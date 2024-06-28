package com.cyberz.ar7demon.repository;

import com.cyberz.ar7demon.model.entity.Admin;
import com.cyberz.ar7demon.model.entity.Master;
import com.cyberz.ar7demon.model.entity.SeniorMaster;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MasterRepository extends JpaRepositoryImplementation<Master,Integer> {
    Optional<Master> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<List<Master>> findByAdmin(Admin admin);

    Optional<List<Master>> findBySeniorMaster(SeniorMaster seniorMaster);
}
