package com.kdev5.cleanpick.contract.domain;

import com.kdev5.cleanpick.contract.domain.enumeration.ContractStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "contract_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public ContractDetail(Contract contract, LocalDateTime checkIn, LocalDateTime checkOut, String pet, ContractStatus status, String request, String housingType) {
        this.contract = contract;
//        this.contractId = contractId; 알아서 매핑된다
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.pet = pet;
        this.status = status;
        this.request = request;
        this.housingType = housingType;
    }

    // 예약 상태 변경 (작업전, 작업후, 정산전, 정산완료)
    public void updateStatus(ContractStatus status) {
        this.status = status;
    }

}