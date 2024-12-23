package ru.bonch.guitarstore.service.impl.converter;

import ru.bonch.guitarstore.dto.GuitarDto;
import ru.bonch.guitarstore.dto.GuitarImagesDto;
import ru.bonch.guitarstore.entity.GuitarEntity;
import ru.bonch.guitarstore.entity.GuitarImageEntity;

import java.util.List;

public final class GuitarConverter {

    public static GuitarDto fromEntity(GuitarEntity guitarEntity) {
        return GuitarDto.builder()
                .guitarId(guitarEntity.getGuitarId())
                .model(guitarEntity.getModel())
                .price(guitarEntity.getPrice())
                .description(guitarEntity.getDescription())
                .guitarImages(
                        guitarEntity.getGuitarImages()
                                .stream()
                                .map(GuitarImageEntity::getImage)
                                .toList()
                )
                .build();
    }

    public static List<GuitarDto> fromEntity(List<GuitarEntity> guitarEntities){
        return guitarEntities
                .stream()
                .map(GuitarConverter::fromEntity)
                .toList();
    }
}
