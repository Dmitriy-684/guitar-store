package ru.bonch.guitarstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.bonch.guitarstore.dto.OrderDto;
import ru.bonch.guitarstore.dto.request.ChangeOrderStatusRequest;
import ru.bonch.guitarstore.dto.request.SaveOrderRequest;
import ru.bonch.guitarstore.entity.OrderEntity;
import ru.bonch.guitarstore.enums.OrderStatus;
import ru.bonch.guitarstore.repository.OrderRepository;
import ru.bonch.guitarstore.service.impl.converter.OrderConverter;
import ru.bonch.guitarstore.service.interfaces.GuitarService;
import ru.bonch.guitarstore.service.interfaces.OrderService;
import ru.bonch.guitarstore.service.interfaces.UserService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultOrderService implements OrderService {

    private final OrderRepository orderRepository;

    private final UserService userService;

    private final GuitarService guitarService;

    @Override
    public OrderDto save(SaveOrderRequest request) {
        var userEntity = userService.getEntityByUserId(request.userId());
        var guitarEntity = guitarService.getEntityByGuitarId(request.guitarId());
        var orderId = UUID.randomUUID().toString().substring(0, 32);

        var orderEntity = OrderEntity.builder()
                .orderId(orderId)
                .user(userEntity)
                .guitar(guitarEntity)
                .status(OrderStatus.PENDING)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        orderRepository.save(orderEntity);

        return OrderConverter.fromEntity(orderEntity);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void changeStatus(ChangeOrderStatusRequest request) {
        var orderId = request.orderId();
        var orderEntity = orderRepository.findOrderByOrderId(orderId)
                .orElseThrow(() -> new IllegalStateException("Order with orderId " + orderId + " not found"));

        orderEntity.setStatus(request.status());

        orderRepository.save(orderEntity);
    }

    @Override
    public List<OrderDto> getAll() {
        var orderEntities = orderRepository.findAll();

        return OrderConverter.fromEntity(orderEntities);
    }
}
