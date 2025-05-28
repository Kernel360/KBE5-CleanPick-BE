package com.kdev5.cleanpick.manager.infra.repository;

import com.kdev5.cleanpick.manager.domain.ManagerAvailableRegion;
import com.kdev5.cleanpick.manager.infra.querydsl.ManagerAvailableRegionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerAvailableRegionRepository extends JpaRepository<ManagerAvailableRegion, Long>, ManagerAvailableRegionRepositoryCustom {
}
