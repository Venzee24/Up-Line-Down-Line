package com.cyberz.ar7demon.repository;

import com.cyberz.ar7demon.model.entity.Admin;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepositoryImplementation<Admin,Integer> {
    Optional<Admin> findByEmail(String email);

    Admin findByName(String name);
}
