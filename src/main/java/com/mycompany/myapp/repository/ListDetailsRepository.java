package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ListDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ListDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ListDetailsRepository extends JpaRepository<ListDetails, Long> {

}
