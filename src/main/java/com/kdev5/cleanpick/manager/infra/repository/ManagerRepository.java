package com.kdev5.cleanpick.manager.infra.repository;

import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.manager.infra.querydsl.ManagerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long>, ManagerRepositoryCustom {

    public Optional<Manager> findByUserId(Long userId);

    public boolean existsById(Long id);
}
