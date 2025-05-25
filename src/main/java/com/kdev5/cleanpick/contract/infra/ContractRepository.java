package com.kdev5.cleanpick.contract.infra;

import com.kdev5.cleanpick.contract.domain.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
}
