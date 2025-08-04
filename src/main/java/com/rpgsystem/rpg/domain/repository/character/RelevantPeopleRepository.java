package com.rpgsystem.rpg.domain.repository.character;

import com.rpgsystem.rpg.domain.entity.RelevantPeopleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelevantPeopleRepository extends JpaRepository<RelevantPeopleEntity, String> {

    List<RelevantPeopleEntity> findAllByCharacter_Id(String characterId);
}
