package com.kdev5.cleanpick.contract.infra;

import com.kdev5.cleanpick.contract.domain.ContractDetail;
import com.kdev5.cleanpick.contract.infra.querydsl.ContractDetailRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractDetailRepository extends JpaRepository<ContractDetail, Long>, ContractDetailRepositoryCustom {
    @Query("SELECT cd FROM ContractDetail cd " +
            "JOIN FETCH cd.contract c " +
            "LEFT JOIN FETCH c.manager " +
            "WHERE cd.contract.id = :contractId")
    Optional<ContractDetail> findByContractId(@Param("contractId") Long contractId);

}
