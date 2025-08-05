package com.rpgsystem.rpg.domain.repository.character;

import com.rpgsystem.rpg.domain.entity.BackgroundEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BackgroundRepository extends JpaRepository<BackgroundEntity, String> {

    Optional<BackgroundEntity> findByCharacter_Id(String characterId);
}
