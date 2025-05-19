package com.kdev5.cleanpick.user.infra.repositories;

import com.kdev5.cleanpick.user.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReginRepository extends JpaRepository<Region, Long> {
}
