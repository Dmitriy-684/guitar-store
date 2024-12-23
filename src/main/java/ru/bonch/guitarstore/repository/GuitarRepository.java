package ru.bonch.guitarstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bonch.guitarstore.entity.GuitarEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuitarRepository extends JpaRepository<GuitarEntity, UUID> {

    Optional<GuitarEntity> findByGuitarId(String guitarId);
}