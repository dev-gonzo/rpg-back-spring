package com.rpgsystem.rpg.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "tb_relevant_people")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelevantPeopleEntity {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "character_id", referencedColumnName = "id", nullable = false)
    private CharacterEntity character;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String name;

    @Column(name = "apparent_age")
    private Integer apparentAge;

    @Column
    private String city;

    @Column
    private String profession;

    @Column(name = "brief_description", columnDefinition = "TEXT")
    private String briefDescription;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
