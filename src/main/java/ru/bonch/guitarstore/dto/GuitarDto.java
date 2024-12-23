package ru.bonch.guitarstore.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GuitarDto(

        String guitarId,

        String model,

        Integer price,

        String description,

        List<byte[]> guitarImages
) {}
