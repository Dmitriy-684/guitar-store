package ru.bonch.guitarstore.service.interfaces;

import ru.bonch.guitarstore.entity.GuitarEntity;
import ru.bonch.guitarstore.entity.GuitarImageEntity;

import java.util.List;
import java.util.Set;

public interface GuitarImagesService {

    Set<GuitarImageEntity> saveImages(GuitarEntity guitarEntity, List<byte[]> images);

    void deleteImagesByGuitar(GuitarEntity guitarEntity);

    Set<GuitarImageEntity> getImagesByGuitar(GuitarEntity guitarEntity);
}
