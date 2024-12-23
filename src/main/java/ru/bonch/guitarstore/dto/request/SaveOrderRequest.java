package ru.bonch.guitarstore.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
public record SaveOrderRequest(

        @JsonProperty("user_id")
        String userId,

        @JsonProperty("guitar_id")
        String guitarId
) {}
