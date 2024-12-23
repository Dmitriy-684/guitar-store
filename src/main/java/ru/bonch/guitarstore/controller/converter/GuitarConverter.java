package ru.bonch.guitarstore.controller.converter;

import ru.bonch.guitarstore.dto.GuitarDto;
import ru.bonch.guitarstore.dto.response.GetAllGuitarResponse;
import ru.bonch.guitarstore.dto.response.GetGuitarResponse;

import java.util.List;

public final class GuitarConverter {

    public static GetGuitarResponse fromDto(GuitarDto guitarDto) {
        return GetGuitarResponse.builder()
                .guitarId(guitarDto.guitarId())
                .model(guitarDto.model())
                .price(guitarDto.price())
                .description(guitarDto.description())
                .images(guitarDto.guitarImages())
                .build();
    }

    public static GetAllGuitarResponse fromDto(List<GuitarDto> guitarDtoList) {
        return GetAllGuitarResponse.builder()
                .guitars(
                        guitarDtoList
                                .stream()
                                .map(GuitarConverter::fromDto)
                                .toList()
                )
                .build();
    }
}
