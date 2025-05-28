package com.kdev5.cleanpick.contract.domain;

import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "contract_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted = false")
public class ContractDetail extends BaseTimeEntity {

    @Id
    @Column(name = "contract_id", nullable = false)
    private Long contractId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    private LocalDateTime checkIn;

    private LocalDateTime checkOut;

    @Column(length = 50)
    private String pet;

    private String request;

    @Column(length = 50, nullable = false)
    private String housingType;


    @Builder
    public ContractDetail(Contract contract, LocalDateTime checkIn, LocalDateTime checkOut, String pet, String request, String housingType) {
        this.contract = contract;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.pet = pet;
        this.request = request;
        this.housingType = housingType;
    }

    public void updateInfo(String request, String pet){
        this.request = request;
        this.pet = pet;
    }


}