package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ReproductionLists;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ReproductionLists entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReproductionListsRepository extends JpaRepository<ReproductionLists, Long> {

}
