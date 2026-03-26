package com.kholty.KH_ABDEV.repository;

import com.kholty.KH_ABDEV.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    // Rechercher un utilisateur par email
    Optional<User> findByEmail(String email);
    
    // Vérifier si un email existe déjà
    boolean existsByEmail(String email);

}
