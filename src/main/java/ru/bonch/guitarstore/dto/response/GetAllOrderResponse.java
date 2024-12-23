package ru.bonch.guitarstore.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record GetAllOrderResponse(

        @JsonProperty("orders")
        List<GetOrderResponse> orders
) {}