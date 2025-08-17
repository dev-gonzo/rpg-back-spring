package com.rpgsystem.rpg.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "tb_ritual_powers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RitualPowerEntity {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "character_id", referencedColumnName = "id", nullable = false)
    private CharacterEntity character;

    @Column(nullable = false)
    private String name;

    @Column(name = "paths_forms", nullable = false)
    private String pathsForms;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "book_page", nullable = false)
    private String bookPage;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
