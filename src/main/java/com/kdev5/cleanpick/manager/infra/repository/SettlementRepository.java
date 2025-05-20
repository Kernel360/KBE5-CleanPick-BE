package com.kdev5.cleanpick.manager.infra.repository;

import com.kdev5.cleanpick.manager.domain.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {

}
