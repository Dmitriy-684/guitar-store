package ru.bonch.guitarstore.service.impl.converter;

import ru.bonch.guitarstore.dto.GuitarImageDto;
import ru.bonch.guitarstore.dto.GuitarImagesDto;
import ru.bonch.guitarstore.entity.GuitarImageEntity;

import java.util.Base64;
import java.util.List;

public final class GuitarImagesConverter {

    public static GuitarImageDto fromEntity(GuitarImageEntity guitarImageEntity) {
        return GuitarImageDto.builder()
                .guitarId(guitarImageEntity.getGuitar().getGuitarId())
                .image(guitarImageEntity.getImage())
                .build();
    }

    public static GuitarImagesDto fromEntity(List<GuitarImageEntity> guitarImageEntities) {
        return GuitarImagesDto.builder()
                .guitarImages(
                        guitarImageEntities.stream().map(GuitarImagesConverter::fromEntity).toList()
                )
                .build();
    }
}
