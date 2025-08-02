package com.rpgsystem.rpg.domain.repository;

import com.rpgsystem.rpg.domain.entity.Character;
import com.rpgsystem.rpg.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CharacterRepository extends JpaRepository<Character, String> {

    @Query("SELECT c FROM Character c WHERE c.controlUser IS NULL ORDER BY c.name ASC")
    List<Character> findAllByNotControlUserOrdered();


    @Query("SELECT c FROM Character c WHERE c.controlUser = :user ORDER BY c.name ASC")
    List<Character> findAllByControlUserOrdered(User user);

    @Query("SELECT c FROM Character c WHERE c.controlUser IS NOT NULL ORDER BY c.name ASC")
    List<Character> findAllByControlUserIsNotNullOrdered();

    @Query("SELECT c FROM Character c WHERE c.controlUser IS NULL AND c.isKnown = true ORDER BY c.name ASC")
    List<Character> findAllByControlUserIsNullAndIsKnownTrueOrdered();

    @Query("SELECT c FROM Character c WHERE c.controlUser IS NOT NULL AND c.controlUser <> :user AND c.isKnown = false ORDER BY c.name ASC")
    List<Character> findAllPrivateControlledByOthersOrdered(User user);
}
