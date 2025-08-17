package com.rpgsystem.rpg.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "tb_paths_and_forms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PathsAndFormsEntity {

    @Id
    @Column(name = "character_id")
    private String characterId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "character_id")
    private CharacterEntity character;

    @Column(name = "understand_form", nullable = false)
    private Integer understandForm;

    @Column(name = "create_form", nullable = false)
    private Integer createForm;

    @Column(name = "control_form", nullable = false)
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
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
