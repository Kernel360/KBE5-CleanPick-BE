package com.kdev5.cleanpick.user.infra.repositories;

import com.kdev5.cleanpick.user.domain.PreferredManager;
import com.kdev5.cleanpick.user.domain.PreferredManagerId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferredManagerRepository extends JpaRepository<PreferredManager, PreferredManagerId> {
}
