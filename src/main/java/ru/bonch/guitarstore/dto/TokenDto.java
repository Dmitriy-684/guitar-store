package ru.bonch.guitarstore.dto;

import lombok.Builder;

@Builder
public record TokenDto(

        String token
) { }
