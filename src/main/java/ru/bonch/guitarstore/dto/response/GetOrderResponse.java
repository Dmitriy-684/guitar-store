package ru.bonch.guitarstore.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;

@Builder
public record GetOrderResponse(

        @JsonProperty("order_id")
        String orderId,

        @JsonProperty("user_phone")
        String userPhone,

        @JsonProperty("user_full_name")
        String userFullName,

        @JsonProperty("guitar_model")
        String guitarModel,

        @JsonProperty("guitar_price")
        Integer guitarPrice,

        @JsonProperty("status")
        String status,

        @JsonProperty("updated_at")
        Instant updatedAt
) {}
