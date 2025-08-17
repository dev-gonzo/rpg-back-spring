package com.rpgsystem.rpg.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "tb_equipments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentEntity {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "character_id", referencedColumnName = "id", nullable = false)
    private CharacterEntity character;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String classification;

    @Column(columnDefinition = "TEXT")
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

    @Column(name = "book_page")
    private String bookPage;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
