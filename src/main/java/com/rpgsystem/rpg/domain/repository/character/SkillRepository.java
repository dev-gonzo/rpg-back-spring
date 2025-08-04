package com.rpgsystem.rpg.domain.repository.character;

import com.rpgsystem.rpg.domain.entity.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<SkillEntity, String> {

    List<SkillEntity> findAllByCharacter_Id(String characterId);
}
