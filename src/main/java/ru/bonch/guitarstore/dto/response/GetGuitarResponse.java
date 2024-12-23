package ru.bonch.guitarstore.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record GetGuitarResponse (

        @JsonProperty("guitar_id")
        String guitarId,

        @JsonProperty("model")
        String model,

        @JsonProperty("price")
        Integer price,

        @JsonProperty("description")
        String description,

        @JsonProperty("images")
        List<byte[]> images
) {}
