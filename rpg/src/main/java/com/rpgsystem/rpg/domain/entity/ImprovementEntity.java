package com.rpgsystem.rpg.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "\"Improvement\"")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImprovementEntity {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "\"characterId\"", nullable = false)
    private CharacterEntity character;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer cost;

    @Column(name = "\"kitValue\"", nullable = false)
    private Integer kitValue;

    @CreationTimestamp
    @Column(name = "\"createdAt\"", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "\"updatedAt\"", nullable = false)
    private Instant updatedAt;
}
