package com.rpgsystem.rpg.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "\"Equipment\"")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentEntity {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "\"characterId\"", referencedColumnName = "id", nullable = false)
    private CharacterEntity character;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String classification;

    @Column
    private String description;

    @Column(name = "\"kineticProtection\"")
    private Integer kineticProtection;

    @Column(name = "\"ballisticProtection\"")
    private Integer ballisticProtection;

    @Column(name = "\"dexterityPenalty\"")
    private Integer dexterityPenalty;

    @Column(name = "\"agilityPenalty\"")
    private Integer agilityPenalty;

    @Column
    private Integer initiative;

    @Column(name = "\"bookPage\"")
    private String bookPage;

    @CreationTimestamp
    @Column(name = "\"createdAt\"", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "\"updatedAt\"", nullable = false)
    private Instant updatedAt;
}
