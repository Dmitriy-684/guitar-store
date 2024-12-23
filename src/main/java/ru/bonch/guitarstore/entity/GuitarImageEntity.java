package ru.bonch.guitarstore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "guitar_images")
public class GuitarImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uid", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guitar_id", referencedColumnName = "guitar_id", nullable = false)
    private GuitarEntity guitar;

    @Column(name = "image", nullable = false)
    private byte[] image;

}