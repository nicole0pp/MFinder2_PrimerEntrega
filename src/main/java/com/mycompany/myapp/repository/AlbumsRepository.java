package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Albums;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Albums entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlbumsRepository extends JpaRepository<Albums, Long> {

}
