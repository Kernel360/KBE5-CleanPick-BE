package com.kdev5.cleanpick.manager.infra.repository;

import com.kdev5.cleanpick.manager.domain.ManagerAvailableCleaning;
import com.kdev5.cleanpick.manager.infra.querydsl.ManagerAvailableCleaningRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerAvailableCleaningRepository extends JpaRepository<ManagerAvailableCleaning, Long>, ManagerAvailableCleaningRepositoryCustom {

}
