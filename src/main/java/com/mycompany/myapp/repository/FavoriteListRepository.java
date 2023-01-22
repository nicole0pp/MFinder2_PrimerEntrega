package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FavoriteList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FavoriteList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavoriteListRepository extends JpaRepository<FavoriteList, Long> {

}
