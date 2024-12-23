package ru.bonch.guitarstore.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UploadGuitarRequest(

        @JsonProperty("model")
        String model,

        @JsonProperty("price")
        Integer price,

        @JsonProperty("description")
        String description,

        @JsonProperty("guitar_images")
        List<byte[]> guitarImages
) {}
