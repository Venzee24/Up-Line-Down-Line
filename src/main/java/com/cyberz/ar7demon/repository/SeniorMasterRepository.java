package com.cyberz.ar7demon.repository;

import com.cyberz.ar7demon.model.entity.Admin;
import com.cyberz.ar7demon.model.entity.SeniorMaster;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeniorMasterRepository extends JpaRepositoryImplementation<SeniorMaster,Integer> {
    Optional<SeniorMaster> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<List<SeniorMaster>> findByAdmin(Admin admin);
}
