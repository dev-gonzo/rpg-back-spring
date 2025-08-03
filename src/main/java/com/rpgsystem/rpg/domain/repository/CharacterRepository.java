package com.rpgsystem.rpg.domain.repository;

import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CharacterRepository extends JpaRepository<CharacterEntity, String> {

    @Query("SELECT c FROM CharacterEntity c WHERE c.controlUser IS NULL ORDER BY c.name ASC")
    List<CharacterEntity> findAllByNotControlUserOrdered();


    @Query("SELECT c FROM CharacterEntity c WHERE c.controlUser = :user ORDER BY c.name ASC")
    List<CharacterEntity> findAllByControlUserOrdered(User user);

    @Query("SELECT c FROM CharacterEntity c WHERE c.controlUser IS NOT NULL ORDER BY c.name ASC")
    List<CharacterEntity> findAllByControlUserIsNotNullOrdered();

    @Query("SELECT c FROM CharacterEntity c WHERE c.controlUser IS NULL AND c.isKnown = true ORDER BY c.name ASC")
    List<CharacterEntity> findAllByControlUserIsNullAndIsKnownTrueOrdered();

    @Query("SELECT c FROM CharacterEntity c WHERE c.controlUser IS NOT NULL AND c.controlUser <> :user AND c.isKnown = false ORDER BY c.name ASC")
    List<CharacterEntity> findAllPrivateControlledByOthersOrdered(User user);
}
