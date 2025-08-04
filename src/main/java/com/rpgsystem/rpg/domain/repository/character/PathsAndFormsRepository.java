package com.rpgsystem.rpg.domain.repository.character;

import com.rpgsystem.rpg.domain.entity.PathsAndFormsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PathsAndFormsRepository extends JpaRepository<PathsAndFormsEntity, String> {

}
