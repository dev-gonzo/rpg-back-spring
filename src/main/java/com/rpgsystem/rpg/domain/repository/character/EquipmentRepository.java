package com.rpgsystem.rpg.domain.repository.character;

import com.rpgsystem.rpg.domain.entity.EquipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<EquipmentEntity, String> {

    List<EquipmentEntity> findAllByCharacter_Id(String characterId);
}
