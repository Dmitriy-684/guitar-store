package ru.bonch.guitarstore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "guitars")
public class GuitarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uid", nullable = false)
    private UUID id;

    @Column(name = "guitar_id", nullable = false)
    private String guitarId;

    @Column(name = "model", nullable = false, unique = true)
    private String model;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    private String description;

    @OneToMany(mappedBy = "guitar")
    private Set<GuitarImageEntity> guitarImages = new LinkedHashSet<>();

    @OneToMany(mappedBy = "guitar")
    private Set<OrderEntity> orders = new LinkedHashSet<>();

}