package ru.bonch.guitarstore.service.impl.converter;

import ru.bonch.guitarstore.dto.OrderDto;
import ru.bonch.guitarstore.entity.OrderEntity;

import java.util.List;

public final class OrderConverter {

    public static OrderDto fromEntity(OrderEntity orderEntity) {
        return OrderDto.builder()
                .orderId(orderEntity.getOrderId())
                .userPhone(orderEntity.getUser().getPhone())
                .userFullName(orderEntity.getUser().getFullName())
                .guitarModel(orderEntity.getGuitar().getModel())
                .guitarPrice(orderEntity.getGuitar().getPrice())
                .status(orderEntity.getStatus())
                .updatedAt(orderEntity.getUpdatedAt())
                .build();
    }

    public static List<OrderDto> fromEntity(List<OrderEntity> orderEntities) {
        return orderEntities
                .stream()
                .map(OrderConverter::fromEntity)
                .toList();
    }
}
