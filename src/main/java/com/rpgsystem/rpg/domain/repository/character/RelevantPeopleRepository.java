package com.rpgsystem.rpg.domain.repository.character;

import com.rpgsystem.rpg.domain.entity.CharacterRelevantPeopleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelevantPeopleRepository extends JpaRepository<CharacterRelevantPeopleEntity, String> {

    List<CharacterRelevantPeopleEntity> findAllByCharacter_Id(String characterId);
}
