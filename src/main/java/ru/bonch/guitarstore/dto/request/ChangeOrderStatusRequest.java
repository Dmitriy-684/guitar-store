package ru.bonch.guitarstore.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.bonch.guitarstore.enums.OrderStatus;

public record ChangeOrderStatusRequest(

        @JsonProperty("order_id")
        String orderId,

        @JsonProperty("status")
        OrderStatus status
) {}
