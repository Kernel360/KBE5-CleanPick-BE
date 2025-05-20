package com.kdev5.cleanpick.customer.infra.repository;

import com.kdev5.cleanpick.customer.domain.PreferredManager;
import com.kdev5.cleanpick.user.domain.PreferredManagerId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferredManagerRepository extends JpaRepository<PreferredManager, PreferredManagerId> {
}
