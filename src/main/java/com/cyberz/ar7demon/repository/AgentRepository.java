package com.cyberz.ar7demon.repository;

import com.cyberz.ar7demon.model.entity.Admin;
import com.cyberz.ar7demon.model.entity.Agent;
import com.cyberz.ar7demon.model.entity.Master;
import com.cyberz.ar7demon.model.entity.SeniorMaster;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepositoryImplementation<Agent,Integer> {
    Optional<Agent> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<List<Agent>> findByAdmin(Admin admin);

    Optional<List<Agent>> findBySeniorMaster(SeniorMaster seniorMaster);

    Optional<List<Agent>> findByMaster(Master master);
}
