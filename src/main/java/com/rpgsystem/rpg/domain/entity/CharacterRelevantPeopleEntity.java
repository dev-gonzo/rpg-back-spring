package com.rpgsystem.rpg.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "\"RelevantPerson\"")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterRelevantPeopleEntity {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "\"characterId\"", referencedColumnName = "id", nullable = false)
    private CharacterEntity character;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String name;

    @Column(name = "\"apparentAge\"")
    private Integer apparentAge;

    @Column
    private String city;

    @Column
    private String profession;

    @Column(name = "\"briefDescription\"")
    private String briefDescription;

    @CreationTimestamp
    @Column(name = "\"createdAt\"", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "\"updatedAt\"", nullable = false)
    private Instant updatedAt;
}
