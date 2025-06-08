package com.kdev5.cleanpick.contract.infra;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.Nominee;
import com.kdev5.cleanpick.contract.infra.querydsl.NomineeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NomineeRepository extends JpaRepository<Nominee, Long>, NomineeRepositoryCustom {
    @Query("SELECT n FROM Nominee n WHERE n.manager.id = :managerId AND n.contract.id = :contractId")
    Optional<Nominee> findByContractAndManager(Long managerId, Long contractId);

    List<Nominee> findAllByContract(Contract contract);
}
