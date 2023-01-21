package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MusicGenres;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MusicGenres entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MusicGenresRepository extends JpaRepository<MusicGenres, Long> {

}
