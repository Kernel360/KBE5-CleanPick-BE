package com.kdev5.cleanpick.contract.domain;

import com.kdev5.cleanpick.contract.domain.enumeration.ContractStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "contract_detail")
public class ContractDetail {

    @Id
    @Column(name = "contract_id", nullable = false)
    private Long contractId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus status;

    @Column(length = 50)
    private String pet;

    private String request;

    @Column(length = 50, nullable = false)
    private String housingType;

}