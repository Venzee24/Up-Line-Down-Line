package com.cyberz.ar7demon.repository;

import com.cyberz.ar7demon.model.entity.*;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepositoryImplementation<AuthenticationToken,Integer> {
    Optional<AuthenticationToken> findByMaster(Master master);

    Optional<AuthenticationToken> findByAgent(Agent agent);

    Optional<AuthenticationToken> findBySeniorMaster(SeniorMaster seniorMaster);

    Optional<AuthenticationToken> findByUser(User user);

    Optional<AuthenticationToken> findByAdmin(Admin dbAdmin);
}
