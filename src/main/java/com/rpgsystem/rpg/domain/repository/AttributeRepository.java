package com.rpgsystem.rpg.domain.repository;


import com.rpgsystem.rpg.domain.entity.AttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttributeRepository extends JpaRepository<AttributeEntity, String> {

    Optional<AttributeEntity> findByCharacter_Id(String characterId);

    boolean existsByCharacter_Id(String characterId);
}
