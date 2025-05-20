package com.kdev5.cleanpick.manager.domain;

import com.kdev5.cleanpick.manager.domain.enumeration.SettlementStatus;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "settlement")
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private Long managerId;

    private Date startDate;

    private Date endDate;

    private Float fee;

    private Float amount;

    @Enumerated(EnumType.STRING)
    private SettlementStatus status;

}
