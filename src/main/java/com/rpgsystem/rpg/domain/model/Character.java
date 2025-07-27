package com.rpgsystem.rpg.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "\"Character\"")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Character {

    @Id
    private String id;


    @Column(nullable = false)
    private String name;

    private String profession;

    @Column(name = "\"birthDate\"", nullable = false)
    private Instant birthDate;

    @Column(name = "\"birthPlace\"")
    private String birthPlace;

    private String gender;

    @Column(name = "\"heightCm\"")
    private Integer heightCm;

    @Column(name = "\"weightKg\"")
    private Integer weightKg;

    private Integer age;

    @Column(name = "\"apparentAge\"")
    private Integer apparentAge;

    private String religion;

    @Column(name = "\"secretSociety\"")
    private String secretSociety;

    private String cabala;

    private String rank;

    private String mentor;

    @ElementCollection
    @CollectionTable(name = "\"Character_societyAllies\"", joinColumns = @JoinColumn(name = "character_id"))
    @Column(name = "value")
    private List<String> societyAllies;

    @Column(name = "\"hitPoints\"")
    private Integer hitPoints;

    @Column(name = "\"currentHitPoints\"")
    private Integer currentHitPoints;

    private Integer initiative;

    @Column(name = "\"currentInitiative\"")
    private Integer currentInitiative;

    @Column(name = "\"heroPoints\"")
    private Integer heroPoints;

    @Column(name = "\"currentHeroPoints\"")
    private Integer currentHeroPoints;

    @Column(name = "\"magicPoints\"")
    private Integer magicPoints;

    @Column(name = "\"currentMagicPoints\"")
    private Integer currentMagicPoints;

    @Column(name = "\"faithPoints\"")
    private Integer faithPoints;

    @Column(name = "\"currentFaithPoints\"")
    private Integer currentFaithPoints;

    @Column(name = "\"protectionIndex\"")
    private Integer protectionIndex;

    @Column(name = "\"currentProtectionIndex\"")
    private Integer currentProtectionIndex;

    private Integer level;

    @Column(name = "\"experiencePoints\"")
    private Integer experiencePoints;

    @Column(name = "\"isKnown\"", nullable = false)
    private boolean isKnown;

    @ManyToOne
    @JoinColumn(name = "\"controlUserId\"")
    private User controlUser;

    @CreationTimestamp
    @Column(name = "\"createdAt\"", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "\"updatedAt\"", nullable = false)
    private Instant updatedAt;

    private String image;

    @Column(nullable = false)
    private boolean edit;
}
