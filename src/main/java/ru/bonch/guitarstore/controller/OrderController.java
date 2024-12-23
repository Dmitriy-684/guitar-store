package ru.bonch.guitarstore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bonch.guitarstore.dto.request.ChangeOrderStatusRequest;
import ru.bonch.guitarstore.dto.request.SaveOrderRequest;
import ru.bonch.guitarstore.controller.converter.OrderConverter;
import ru.bonch.guitarstore.enums.UserRole;
import ru.bonch.guitarstore.security.JwtUtil;
import ru.bonch.guitarstore.service.interfaces.OrderService;
import ru.bonch.guitarstore.service.interfaces.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("secured/order")
public class OrderController {

    private final OrderService orderService;

    private final JwtUtil jwtUtil;

    private final UserService userService;

    @PostMapping("/create")
    ResponseEntity<?> saveOrder(@RequestBody SaveOrderRequest request){
        var orderDto = orderService.save(request);

        return ResponseEntity.ok(OrderConverter.fromDto(orderDto));
    }

    @GetMapping("/all")
    ResponseEntity<?> getAll(@RequestHeader("Authorization") String authorizationHeader) {
        var username = jwtUtil.extractUsernameFromHeader(authorizationHeader);

        if (userService.isUserHasRole(username, UserRole.MANAGER)) {
            var orderDtoList = orderService.getAll();

            return ResponseEntity.ok(OrderConverter.fromDto(orderDtoList));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("User with username " + username + " is not manager");
    }

    @PostMapping("/change/status")
    ResponseEntity<?> changeStatus(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody ChangeOrderStatusRequest request
    ) {
        var username = jwtUtil.extractUsernameFromHeader(authorizationHeader);

        if (userService.isUserHasRole(username, UserRole.MANAGER)) {
            orderService.changeStatus(request);

            return ResponseEntity.ok().body("Статус заказа успешно изменён на " + request.status());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("User with username " + username + " is not manager");
    }
}
