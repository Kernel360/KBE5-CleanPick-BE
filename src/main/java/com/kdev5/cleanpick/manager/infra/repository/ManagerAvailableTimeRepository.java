package com.kdev5.cleanpick.manager.infra.repository;

import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.manager.domain.ManagerAvailableTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface ManagerAvailableTimeRepository extends JpaRepository<ManagerAvailableTime, Long> {
    List<ManagerAvailableTime> findByManagerAndDayOfWeek(Manager manager, DayOfWeek dayOfWeek);

    List<ManagerAvailableTime> findByDayOfWeekAndManagerIdIn(DayOfWeek dayOfWeek, List<Long> managerIds);
}
