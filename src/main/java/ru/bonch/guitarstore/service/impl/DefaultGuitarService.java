package ru.bonch.guitarstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.bonch.guitarstore.dto.GuitarDto;
import ru.bonch.guitarstore.dto.GuitarImageDto;
import ru.bonch.guitarstore.dto.request.UpdateGuitarRequest;
import ru.bonch.guitarstore.dto.request.UploadGuitarRequest;
import ru.bonch.guitarstore.entity.GuitarEntity;
import ru.bonch.guitarstore.repository.GuitarRepository;
import ru.bonch.guitarstore.service.impl.converter.GuitarConverter;
import ru.bonch.guitarstore.service.interfaces.GuitarImagesService;
import ru.bonch.guitarstore.service.interfaces.GuitarService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultGuitarService implements GuitarService {

    private final GuitarRepository guitarRepository;

    private final GuitarImagesService guitarImagesService;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public GuitarDto save(UploadGuitarRequest request) {
        var guitarId = UUID.randomUUID().toString().substring(0, 32);

        var guitarEntity = GuitarEntity.builder()
                .guitarId(guitarId)
                .model(request.model())
                .price(request.price())
                .description(request.description())
                .build();

        guitarRepository.save(guitarEntity);

        var guitarImageEntities = guitarImagesService.saveImages(guitarEntity, request.guitarImages());

        guitarEntity.setGuitarImages(guitarImageEntities);

        return GuitarConverter.fromEntity(guitarEntity);
    }

    @Override
    public List<GuitarDto> getAll() {
        var guitarEntities = guitarRepository.findAll();

        return GuitarConverter.fromEntity(guitarEntities);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updateGuitar(UpdateGuitarRequest request) {
        var guitarEntity = findByGuitarId(request.guitar_id());

        guitarEntity.setModel(request.model());
        guitarEntity.setPrice(request.price());
        guitarEntity.setDescription(request.description());

        if (request.guitarImages() != null && !request.guitarImages().isEmpty()) {
            guitarImagesService.deleteImagesByGuitar(guitarEntity);
            guitarEntity.setGuitarImages(
                    guitarImagesService.saveImages(guitarEntity, request.guitarImages())
            );
        }

        guitarRepository.save(guitarEntity);
    }

    @Override
    public GuitarDto getByGuitarId(String guitarId) {
        var guitarEntity = findByGuitarId(guitarId);

        return GuitarConverter.fromEntity(guitarEntity);
    }

    @Override
    public GuitarEntity getEntityByGuitarId(String guitarId) {
        return findByGuitarId(guitarId);
    }

    private GuitarEntity findByGuitarId(String guitarId) {
        return guitarRepository.findByGuitarId(guitarId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Guitar with guitarId " + guitarId + " not found")
                );
    }
}
