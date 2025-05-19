package com.kdev5.cleanpick.contract.infra;

import com.kdev5.cleanpick.contract.domain.ContractOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractOptionRepository extends JpaRepository<ContractOption, Long> {
}
