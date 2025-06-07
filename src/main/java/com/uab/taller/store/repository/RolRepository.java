package com.uab.taller.store.repository;

import com.uab.taller.store.domain.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNameIgnoreCase(String name);
}
