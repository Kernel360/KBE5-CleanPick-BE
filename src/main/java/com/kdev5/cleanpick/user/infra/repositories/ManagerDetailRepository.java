package com.kdev5.cleanpick.user.infra.repositories;

import com.kdev5.cleanpick.user.domain.ManagerDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerDetailRepository extends JpaRepository<ManagerDetail, Long> {
}
