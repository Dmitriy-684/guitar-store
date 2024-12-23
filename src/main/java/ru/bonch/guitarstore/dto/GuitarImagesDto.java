package ru.bonch.guitarstore.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GuitarImagesDto (

        List<GuitarImageDto> guitarImages
) {}
