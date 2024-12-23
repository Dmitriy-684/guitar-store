package ru.bonch.guitarstore.dto;

import lombok.Builder;
import ru.bonch.guitarstore.enums.OrderStatus;

import java.time.Instant;

@Builder
public record OrderDto(

        String orderId,

        String userPhone,

        String userFullName,

        String guitarModel,

        Integer guitarPrice,

        OrderStatus status,

        Instant updatedAt
) {}
