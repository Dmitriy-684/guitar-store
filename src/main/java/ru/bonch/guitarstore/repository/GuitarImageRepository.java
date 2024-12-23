package ru.bonch.guitarstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bonch.guitarstore.entity.GuitarEntity;
import ru.bonch.guitarstore.entity.GuitarImageEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface GuitarImageRepository extends JpaRepository<GuitarImageEntity, UUID> {

    List<GuitarImageEntity> findByGuitar(GuitarEntity guitarEntity);
    void deleteAllByGuitar(GuitarEntity guitarEntity);
}