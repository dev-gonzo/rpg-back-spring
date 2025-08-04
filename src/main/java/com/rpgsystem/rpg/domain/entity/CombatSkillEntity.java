package com.rpgsystem.rpg.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "\"CombatSkill\"")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CombatSkillEntity {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "\"characterId\"", referencedColumnName = "id", nullable = false)
    private CharacterEntity character;

    @Column(name = "\"group\"")
    private String group;

    @Column(name = "skill", nullable = false)
    private String skill;

    @Column(name = "attribute")
    private String attribute;

    @Column(name = "\"attackCost\"", nullable = false)
    private Integer attackCost;

    @Column(name = "\"defenseCost\"", nullable = false)
    private Integer defenseCost;

    @Column(name = "\"attackKitValue\"", nullable = false)
    private Integer attackKitValue;

    @Column(name = "\"defenseKitValue\"", nullable = false)
    private Integer defenseKitValue;

    @CreationTimestamp
    @Column(name = "\"createdAt\"", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "\"updatedAt\"", nullable = false)
    private Instant updatedAt;
}
