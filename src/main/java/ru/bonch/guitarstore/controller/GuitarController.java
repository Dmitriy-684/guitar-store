package ru.bonch.guitarstore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bonch.guitarstore.controller.converter.GuitarConverter;
import ru.bonch.guitarstore.dto.request.UpdateGuitarRequest;
import ru.bonch.guitarstore.dto.request.UploadGuitarRequest;
import ru.bonch.guitarstore.enums.UserRole;
import ru.bonch.guitarstore.security.JwtUtil;
import ru.bonch.guitarstore.service.interfaces.GuitarService;
import ru.bonch.guitarstore.service.interfaces.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/secured/guitar")
public class GuitarController {

    private final GuitarService guitarService;

    private final UserService userService;

    private final JwtUtil jwtUtil;


    @GetMapping("/all")
    ResponseEntity<?> getAllGuitar() {
        var guitarDtoList = guitarService.getAll();

        if (guitarDtoList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(GuitarConverter.fromDto(guitarDtoList));
    }

    @GetMapping("/{guitarId}")
    ResponseEntity<?> getGuitar(@PathVariable String guitarId) {
        try {
            var guitarDto = guitarService.getByGuitarId(guitarId);
            return ResponseEntity.ok(GuitarConverter.fromDto(guitarDto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Гитары с id " + guitarId + " не существует");
        }
    }


    @PostMapping("/upload")
    public ResponseEntity<?> uploadGuitar(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UploadGuitarRequest request
    ) {
        var username = jwtUtil.extractUsernameFromHeader(authorizationHeader);

        if (userService.isUserHasRole(username, UserRole.MANAGER)) {
            var guitarDto = guitarService.save(request);

            return ResponseEntity.ok(GuitarConverter.fromDto(guitarDto));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("User with username " + username + " is not manager");
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateGuitar(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UpdateGuitarRequest request
    ) {
        var username = jwtUtil.extractUsernameFromHeader(authorizationHeader);

        if (userService.isUserHasRole(username, UserRole.MANAGER)) {

            guitarService.updateGuitar(request);

            return ResponseEntity.ok().body("Гитара с моделью + " + request.model()  + " успешно обновлена");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("User with username " + username + " is not manager");
    }
}
