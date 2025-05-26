package com.kdev5.cleanpick.contract.domain;

import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import com.kdev5.cleanpick.manager.domain.Manager;
import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Table(name = "nominee")
public class Nominee extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private Manager manager;

    @Column(name = "is_accepted", nullable = false)
    private boolean isAccepted;

    @Builder
    public Nominee(Contract contract, Manager manager, boolean isAccepted) {
        this.contract = contract;
        this.manager = manager;
        this.isAccepted = isAccepted;
    }
}

