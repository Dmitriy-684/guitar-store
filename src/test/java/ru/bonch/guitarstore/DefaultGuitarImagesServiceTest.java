package ru.bonch.guitarstore;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bonch.guitarstore.entity.GuitarEntity;
import ru.bonch.guitarstore.entity.GuitarImageEntity;
import ru.bonch.guitarstore.repository.GuitarImageRepository;
import ru.bonch.guitarstore.service.impl.DefaultGuitarImagesService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyList;

@ExtendWith(MockitoExtension.class)
public class DefaultGuitarImagesServiceTest {

    @Mock
    private GuitarImageRepository guitarImageRepository;

    @InjectMocks
    private DefaultGuitarImagesService guitarImagesService;

    @Test
    void saveImages_ShouldSaveAllImagesAndReturnEntities() {
        // Arrange
        GuitarEntity guitarEntity = GuitarEntity.builder()
                .guitarId("guitar-id")
                .model("Guitar Model")
                .price(1000)
                .description("Guitar Description")
                .build();

        List<byte[]> images = List.of(new byte[]{1, 2, 3}, new byte[]{4, 5, 6});

        GuitarImageEntity image1 = GuitarImageEntity.builder()
                .guitar(guitarEntity)
                .image(images.get(0))
                .build();

        GuitarImageEntity image2 = GuitarImageEntity.builder()
                .guitar(guitarEntity)
                .image(images.get(1))
                .build();

        List<GuitarImageEntity> guitarImageEntities = List.of(image1, image2);

        Mockito.when(guitarImageRepository.saveAll(Mockito.anySet()))
                .thenReturn(guitarImageEntities);

        // Act
        guitarImagesService.saveImages(guitarEntity, images);
    }

    @Test
    void deleteImagesByGuitar_ShouldDeleteImagesByGuitarEntity() {
        // Arrange
        GuitarEntity guitarEntity = GuitarEntity.builder()
                .guitarId("guitar-id")
                .build();

        // Act
        guitarImagesService.deleteImagesByGuitar(guitarEntity);

        // Assert
        Mockito.verify(guitarImageRepository).deleteAllByGuitar(guitarEntity);
    }

    @Test
    void getImagesByGuitar_ShouldReturnImages() {
        // Arrange
        GuitarEntity guitarEntity = GuitarEntity.builder()
                .guitarId("guitar-id")
                .build();

        List<GuitarImageEntity> images = List.of(
                GuitarImageEntity.builder().guitar(guitarEntity).image(new byte[]{1, 2, 3}).build(),
                GuitarImageEntity.builder().guitar(guitarEntity).image(new byte[]{4, 5, 6}).build()
        );

        Mockito.when(guitarImageRepository.findByGuitar(guitarEntity)).thenReturn(images);

        // Act
        Set<GuitarImageEntity> result = guitarImagesService.getImagesByGuitar(guitarEntity);

        // Assert
        Assertions.assertEquals(images.size(), result.size());
        Assertions.assertEquals(new HashSet<>(images), result);
    }
}

