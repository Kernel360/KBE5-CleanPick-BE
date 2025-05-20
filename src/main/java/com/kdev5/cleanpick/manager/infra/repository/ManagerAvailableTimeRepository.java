package com.kdev5.cleanpick.manager.infra.repository;

import com.kdev5.cleanpick.manager.domain.ManagerAvailableTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerAvailableTimeRepository extends JpaRepository<ManagerAvailableTime, Long> {
}
