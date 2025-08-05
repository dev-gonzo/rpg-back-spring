package com.rpgsystem.rpg.domain.repository.character;

import com.rpgsystem.rpg.domain.entity.RitualPowerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RitualPowerRepository extends JpaRepository<RitualPowerEntity, String> {

    List<RitualPowerEntity> findAllByCharacter_Id(String characterId);
}
