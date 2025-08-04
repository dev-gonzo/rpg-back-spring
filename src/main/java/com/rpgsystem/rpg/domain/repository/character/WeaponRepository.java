package com.rpgsystem.rpg.domain.repository.character;

import com.rpgsystem.rpg.domain.entity.WeaponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeaponRepository extends JpaRepository<WeaponEntity, String> {

    List<WeaponEntity> findAllByCharacter_Id(String characterId);
}
