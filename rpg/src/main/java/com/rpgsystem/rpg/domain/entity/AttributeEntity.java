package com.rpgsystem.rpg.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "\"Attribute\"")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeEntity {

    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "\"characterId\"", referencedColumnName = "id", nullable = false, unique = true)
    private CharacterEntity character;

    @Column(name = "\"CON\"", nullable = false)
    private Integer con;

    @Column(name = "\"FR\"", nullable = false)
    private Integer fr;

    @Column(name = "\"DEX\"", nullable = false)
    private Integer dex;

    @Column(name = "\"AGI\"", nullable = false)
    private Integer agi;

    @Column(name = "\"INT\"", nullable = false)
    private Integer intel;

    @Column(name = "\"WILL\"", nullable = false)
    private Integer will;

    @Column(name = "\"PER\"", nullable = false)
    private Integer per;

    @Column(name = "\"CAR\"", nullable = false)
    private Integer car;

    @Column(name = "con_mod")
    private Integer conMod;

    @Column(name = "fr_mod")
    private Integer frMod;

    @Column(name = "dex_mod")
    private Integer dexMod;

    @Column(name = "agi_mod")
    private Integer agiMod;

    @Column(name = "int_mod")
    private Integer intMod;

    @Column(name = "will_mod")
    private Integer willMod;

    @Column(name = "per_mod")
    private Integer perMod;

    @Column(name = "car_mod")
    private Integer carMod;

    @CreationTimestamp
    @Column(name = "\"createdAt\"", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "\"updatedAt\"", nullable = false)
    private Instant updatedAt;
}
