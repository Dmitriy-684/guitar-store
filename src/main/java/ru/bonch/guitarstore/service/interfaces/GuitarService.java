package ru.bonch.guitarstore.service.interfaces;

import ru.bonch.guitarstore.dto.GuitarDto;
import ru.bonch.guitarstore.dto.request.UpdateGuitarRequest;
import ru.bonch.guitarstore.dto.request.UploadGuitarRequest;
import ru.bonch.guitarstore.entity.GuitarEntity;

import java.util.List;

public interface GuitarService {

    GuitarDto save(UploadGuitarRequest request);

    List<GuitarDto> getAll();

    void updateGuitar(UpdateGuitarRequest request);

    GuitarDto getByGuitarId(String guitarId);

    GuitarEntity getEntityByGuitarId(String guitarId);
}
