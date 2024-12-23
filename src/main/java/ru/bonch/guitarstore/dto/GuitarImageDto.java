package ru.bonch.guitarstore.dto;

import lombok.Builder;

@Builder
public record GuitarImageDto(

        String guitarId,

        byte[] image
) {}
