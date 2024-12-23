package ru.bonch.guitarstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bonch.guitarstore.dto.GuitarImagesDto;
import ru.bonch.guitarstore.entity.GuitarEntity;
import ru.bonch.guitarstore.entity.GuitarImageEntity;
import ru.bonch.guitarstore.repository.GuitarImageRepository;
import ru.bonch.guitarstore.service.impl.converter.GuitarImagesConverter;
import ru.bonch.guitarstore.service.interfaces.GuitarImagesService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultGuitarImagesService implements GuitarImagesService {

    private final GuitarImageRepository guitarImageRepository;

    @Override
    public Set<GuitarImageEntity> saveImages(GuitarEntity guitarEntity, List<byte[]> images) {

        var guitarImageEntities = images.stream()
                .map(image -> GuitarImageEntity.builder()
                        .guitar(guitarEntity)
                        .image(image)
                        .build())
                .collect(Collectors.toSet());

        guitarImageRepository.saveAll(guitarImageEntities);

        return guitarImageEntities;
    }

    @Override
    public void deleteImagesByGuitar(GuitarEntity guitarEntity) {
        guitarImageRepository.deleteAllByGuitar(guitarEntity);
    }

    @Override
    public Set<GuitarImageEntity> getImagesByGuitar(GuitarEntity guitarEntity) {
        return new HashSet<>(guitarImageRepository.findByGuitar(guitarEntity));
    }

}
