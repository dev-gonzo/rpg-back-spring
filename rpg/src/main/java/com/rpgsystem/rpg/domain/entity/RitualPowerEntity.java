package com.rpgsystem.rpg.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "\"Ritual\"")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RitualPowerEntity {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "\"characterId\"", referencedColumnName = "id", nullable = false)
    private CharacterEntity character;

    @Column(nullable = false)
    private String name;

    @Column(name = "\"pathsForms\"", nullable = false)
    private String pathsForms;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "\"bookPage\"", nullable = false)
    private String bookPage;

    @CreationTimestamp
    @Column(name = "\"createdAt\"", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "\"updatedAt\"", nullable = false)
    private Instant updatedAt;
}
