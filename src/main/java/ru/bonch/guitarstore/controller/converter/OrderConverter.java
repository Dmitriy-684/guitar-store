package ru.bonch.guitarstore.controller.converter;

import ru.bonch.guitarstore.dto.OrderDto;
import ru.bonch.guitarstore.dto.response.GetAllOrderResponse;
import ru.bonch.guitarstore.dto.response.GetOrderResponse;

import java.util.List;

public final class OrderConverter {

    public static GetOrderResponse fromDto(OrderDto orderDto) {
        return GetOrderResponse.builder()
                .orderId(orderDto.orderId())
                .userPhone(orderDto.userPhone())
                .userFullName(orderDto.userFullName())
                .guitarModel(orderDto.guitarModel())
                .guitarPrice(orderDto.guitarPrice())
                .status(orderDto.status().name())
                .updatedAt(orderDto.updatedAt())
                .build();
    }

    public static GetAllOrderResponse fromDto(List<OrderDto> orderDtoList) {
        return GetAllOrderResponse.builder()
                .orders(
                        orderDtoList
                                .stream()
                                .map(OrderConverter::fromDto)
                                .toList()
                )
                .build();
    }
}
