package com.rpgsystem.rpg.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "\"PathsAndForms\"")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PathsAndFormsEntity {

    @Id
    @Column(name = "\"characterId\"")
    private String characterId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "\"characterId\"")
    private CharacterEntity character;

    @Column(name = "\"understandForm\"", nullable = false)
    private Integer understandForm;

    @Column(name = "\"createForm\"", nullable = false)
    private Integer createForm;

    @Column(name = "\"controlForm\"", nullable = false)
    private Integer controlForm;

    @Column(nullable = false)
    private Integer fire;

    @Column(nullable = false)
    private Integer water;

    @Column(nullable = false)
    private Integer earth;

    @Column(nullable = false)
    private Integer air;

    @Column(nullable = false)
    private Integer light;

    @Column(nullable = false)
    private Integer darkness;

    @Column(nullable = false)
    private Integer plants;

    @Column(nullable = false)
    private Integer animals;

    @Column(nullable = false)
    private Integer humans;

    @Column(nullable = false)
    private Integer spiritum;

    @Column(nullable = false)
    private Integer arkanun;

    @Column(nullable = false)
    private Integer metamagic;

    @CreationTimestamp
    @Column(name = "\"createdAt\"", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "\"updatedAt\"", nullable = false)
    private Instant updatedAt;
}
