package com.rpgsystem.rpg.domain.repository.character;

import com.rpgsystem.rpg.domain.entity.CombatSkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CombatSkillRepository extends JpaRepository<CombatSkillEntity, String> {

    List<CombatSkillEntity> findAllByCharacter_Id(String characterId);
}
