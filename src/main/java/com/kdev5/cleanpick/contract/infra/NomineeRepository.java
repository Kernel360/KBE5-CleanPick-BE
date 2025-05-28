package com.kdev5.cleanpick.contract.infra;

import com.kdev5.cleanpick.contract.domain.Nominee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NomineeRepository extends JpaRepository<Nominee, Long> {
    @Query("SELECT n FROM Nominee n WHERE n.manager.id = :managerId AND n.contract.id = :contractId")
    Optional<Nominee> findByContractAndManager(Long managerId, Long contractId);
}
