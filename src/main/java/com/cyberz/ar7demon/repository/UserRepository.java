package com.cyberz.ar7demon.repository;

import com.cyberz.ar7demon.model.entity.*;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepositoryImplementation<User,Integer> {
    Optional<User> findByEmail(String username);

    boolean existsByEmail(String email);

    Optional<List<User>> findByAdmin(Admin admin);

    Optional<List<User>> findByAgent(Agent agent);

    Optional<List<User>> findBySeniorMaster(SeniorMaster seniorMaster);

    Optional<List<User>> findByMaster(Master master);
}
