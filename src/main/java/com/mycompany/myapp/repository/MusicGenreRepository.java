package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MusicGenre;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MusicGenre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MusicGenreRepository extends JpaRepository<MusicGenre, Long> {

}
