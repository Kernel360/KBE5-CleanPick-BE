package com.kdev5.cleanpick.contract.domain;

import com.kdev5.cleanpick.contract.domain.enumeration.MatchingStatus;
import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import com.kdev5.cleanpick.manager.domain.Manager;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "nominee")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Nominee extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private Manager manager;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchingStatus status;

    @Builder
    public Nominee(Contract contract, Manager manager) {
        this.contract = contract;
        this.manager = manager;
        this.status = MatchingStatus.PENDING;
    }

    public void updateStatus(MatchingStatus status) {
        this.status = status;
    }
}

