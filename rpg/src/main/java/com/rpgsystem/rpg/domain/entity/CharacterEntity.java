package com.rpgsystem.rpg.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tb_characters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterEntity {

    @Id
    private String id;


    @Column(nullable = false)
    private String name;

    private String profession;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "birth_place")
    private String birthPlace;

    private String gender;

    @Column(name = "height_cm")
    private Integer heightCm;

    @Column(name = "weight_kg")
    private Integer weightKg;

    private Integer age;

    @Column(name = "apparent_age")
    private Integer apparentAge;

    private String religion;

    @Column(name = "secret_society")
    private String secretSociety;

    private String cabala;

    private String rank;

    private String mentor;

    @ElementCollection
    @CollectionTable(name = "tb_character_society_allies", joinColumns = @JoinColumn(name = "character_id"))
    @Column(name = "\"value\"")
    private List<String> societyAllies;

    @Column(name = "hit_points")
    private Integer hitPoints;

    @Column(name = "current_hit_points")
    private Integer currentHitPoints;

    private Integer initiative;

    @Column(name = "current_initiative")
    private Integer currentInitiative;

    @Column(name = "hero_points")
    private Integer heroPoints;

    @Column(name = "current_hero_points")
    private Integer currentHeroPoints;

    @Column(name = "magic_points")
    private Integer magicPoints;

    @Column(name = "current_magic_points")
    private Integer currentMagicPoints;

    @Column(name = "faith_points")
    private Integer faithPoints;

    @Column(name = "current_faith_points")
    private Integer currentFaithPoints;

    @Column(name = "protection_index")
    private Integer protectionIndex;

    @Column(name = "current_protection_index")
    private Integer currentProtectionIndex;

    private Integer level;

    @Column(name = "experience_points")
    private Integer experiencePoints;

    @Column(name = "is_known", nullable = false)
    private boolean isKnown;

    @ManyToOne
    @JoinColumn(name = "control_user_id")
    private User controlUser;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Column(nullable = false)
    private boolean edit;
}
