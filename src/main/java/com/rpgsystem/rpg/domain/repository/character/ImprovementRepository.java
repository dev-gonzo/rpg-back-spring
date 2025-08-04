package com.rpgsystem.rpg.domain.repository.character;

import com.rpgsystem.rpg.domain.entity.ImprovementEntity;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImprovementRepository extends JpaRepository<ImprovementEntity, String> {

    List<ImprovementEntity> findAllByCharacter(CharacterEntity character);

    List<ImprovementEntity> findAllByCharacter_Id(String characterId);

    void deleteAllByCharacter(CharacterEntity character);
}
