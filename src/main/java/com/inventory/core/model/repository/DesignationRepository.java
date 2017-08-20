package com.inventory.core.model.repository;

import com.inventory.core.model.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * Created by dhiraj on 8/11/17.
 */
@Repository
@Transactional(readOnly = true)
public interface DesignationRepository extends JpaRepository<Designation , Long> , JpaSpecificationExecutor<Designation>{

    @Lock(LockModeType.OPTIMISTIC)
    Designation findById(long designationId);

    @Lock(LockModeType.OPTIMISTIC)
    Designation findByTitle(long title);

    @Query("select d from Designation d order by d.title asc ")
    List<Designation> findAll();
}
