package ru.bonch.guitarstore;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bonch.guitarstore.dto.GuitarDto;
import ru.bonch.guitarstore.dto.request.UpdateGuitarRequest;
import ru.bonch.guitarstore.dto.request.UploadGuitarRequest;
import ru.bonch.guitarstore.entity.GuitarEntity;
import ru.bonch.guitarstore.entity.GuitarImageEntity;
import ru.bonch.guitarstore.repository.GuitarRepository;
import ru.bonch.guitarstore.service.impl.DefaultGuitarService;
import ru.bonch.guitarstore.service.interfaces.GuitarImagesService;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class DefaultGuitarServiceTest {

    @Mock
    private GuitarRepository guitarRepository;

    @Mock
    private GuitarImagesService guitarImagesService;

    @InjectMocks
    private DefaultGuitarService guitarService;

    @Test
    void save_ShouldSaveGuitarAndReturnDto() {
        // Arrange
        UploadGuitarRequest request = new UploadGuitarRequest(
                "Model 1",
                1000,
                "A great guitar",
                List.of("image1".getBytes(), "image2".getBytes())
        );

        GuitarEntity savedEntity = GuitarEntity.builder()
                .guitarId("generated-id")
                .model(request.model())
                .price(request.price())
                .description(request.description())
                .build();

        Set<GuitarImageEntity> savedImages = Set.of(
                GuitarImageEntity.builder()
                        .id(UUID.randomUUID())
                        .guitar(savedEntity)
                        .image("image1".getBytes())
                        .build(),
                GuitarImageEntity.builder()
                        .id(UUID.randomUUID())
                        .guitar(savedEntity)
                        .image("image2".getBytes())
                        .build()
        );

        Mockito.when(guitarRepository.save(Mockito.any(GuitarEntity.class))).thenReturn(savedEntity);
        Mockito.when(guitarImagesService.saveImages(Mockito.any(GuitarEntity.class), Mockito.anyList()))
                .thenReturn(savedImages);

        // Act
        GuitarDto result = guitarService.save(request);

        // Assert
        ArgumentCaptor<GuitarEntity> captor = ArgumentCaptor.forClass(GuitarEntity.class);
        Mockito.verify(guitarRepository).save(captor.capture());

        GuitarEntity capturedEntity = captor.getValue();
        Assertions.assertNotNull(capturedEntity.getGuitarId());
        Assertions.assertEquals(request.model(), capturedEntity.getModel());
        Assertions.assertEquals(request.price(), capturedEntity.getPrice());
        Assertions.assertEquals(request.description(), capturedEntity.getDescription());

        Assertions.assertEquals(request.model(), result.model());
        Assertions.assertEquals(request.price(), result.price());
        Assertions.assertEquals(request.description(), result.description());
        Assertions.assertEquals(2, result.guitarImages().size());
    }

    @Test
    void updateGuitar_ShouldUpdateEntityAndSave() {
        // Arrange
        GuitarEntity existingEntity = GuitarEntity.builder()
                .guitarId("guitar-1")
                .model("Old Model")
                .price(1000)
                .description("Old Description")
                .build();

        UpdateGuitarRequest request = new UpdateGuitarRequest(
                "guitar-1",
                "New Model",
                1200,
                "New Description",
                List.of("newImage".getBytes())
        );

        Set<GuitarImageEntity> newImages = Set.of(
                GuitarImageEntity.builder()
                        .id(UUID.randomUUID())
                        .guitar(existingEntity)
                        .image("newImage".getBytes())
                        .build()
        );

        Mockito.when(guitarRepository.findByGuitarId("guitar-1")).thenReturn(Optional.of(existingEntity));
        Mockito.when(guitarImagesService.saveImages(existingEntity, request.guitarImages())).thenReturn(newImages);

        // Act
        guitarService.updateGuitar(request);

        // Assert
        Mockito.verify(guitarImagesService).deleteImagesByGuitar(existingEntity);
        Mockito.verify(guitarImagesService).saveImages(existingEntity, request.guitarImages());
        Mockito.verify(guitarRepository).save(existingEntity);

        Assertions.assertEquals("New Model", existingEntity.getModel());
        Assertions.assertEquals(1200, existingEntity.getPrice());
        Assertions.assertEquals("New Description", existingEntity.getDescription());
        Assertions.assertEquals(1, existingEntity.getGuitarImages().size());
    }

    @Test
    void getByGuitarId_ShouldReturnDtoWithImages() {
        // Arrange
        GuitarEntity entity = GuitarEntity.builder()
                .guitarId("guitar-1")
                .model("Model 1")
                .price(1000)
                .description("Description 1")
                .build();

        List<GuitarImageEntity> images = List.of(
                GuitarImageEntity.builder()
                        .id(UUID.randomUUID())
                        .guitar(entity)
                        .image("image1".getBytes())
                        .build(),
                GuitarImageEntity.builder()
                        .id(UUID.randomUUID())
                        .guitar(entity)
                        .image("image2".getBytes())
                        .build()
        );

        entity.setGuitarImages(new HashSet<>(images));

        Mockito.when(guitarRepository.findByGuitarId("guitar-1")).thenReturn(Optional.of(entity));

        // Act
        GuitarDto result = guitarService.getByGuitarId("guitar-1");

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals("guitar-1", result.guitarId());
        Assertions.assertEquals("Model 1", result.model());
        Assertions.assertEquals(1000, result.price());
        Assertions.assertEquals(2, result.guitarImages().size());
    }
}
