package com.kdev5.cleanpick.contract.infra;

import com.kdev5.cleanpick.contract.domain.ContractDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractDetailRepository extends JpaRepository<ContractDetail, Long> {
}
