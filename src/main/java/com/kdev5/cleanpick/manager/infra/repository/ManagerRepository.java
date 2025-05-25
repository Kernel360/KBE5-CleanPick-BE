package com.kdev5.cleanpick.manager.infra.repository;

import com.kdev5.cleanpick.manager.domain.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    public Optional<Manager> findByUserId(Long userId);

    public boolean existsById(Long id);
}
