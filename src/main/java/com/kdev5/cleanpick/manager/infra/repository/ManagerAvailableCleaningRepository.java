package com.kdev5.cleanpick.manager.infra.repository;

import com.kdev5.cleanpick.cleaning.domain.Cleaning;
import com.kdev5.cleanpick.manager.domain.ManagerAvailableCleaning;
import com.kdev5.cleanpick.manager.infra.querydsl.ManagerAvailableCleaningRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ManagerAvailableCleaningRepository extends JpaRepository<ManagerAvailableCleaning, Long>, ManagerAvailableCleaningRepositoryCustom {
    @Query("SELECT m.manager.id FROM ManagerAvailableCleaning m WHERE m.cleaning = :cleaning AND m.manager.id IN :managerIds")
    List<Long> findByCleaningAndManagerIdIn(Cleaning cleaning, List<Long> managerIds);

}
