package com.kdev5.cleanpick.contract.domain;

import com.kdev5.cleanpick.cleaning.domain.Cleaning;
import com.kdev5.cleanpick.contract.domain.enumeration.ContractStatus;
import com.kdev5.cleanpick.customer.domain.Customer;
import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import com.kdev5.cleanpick.manager.domain.Manager;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "contract")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "deleted_at IS NULL")
public class Contract extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cleaning_id", nullable = false)
    private Cleaning cleaning;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_contract_id")
    private RoutineContract routineContract;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus status;

    private LocalDateTime contractDate;

    @Column(length = 255, nullable = false)
    private String address;

    @Column(length = 255, nullable = true)
    private String subAddress;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false)
    private int totalTime;

    @Column(name = "is_personal", nullable = false)
    private boolean personal;

    @Builder
    public Contract(Customer customer, Manager manager, Cleaning cleaning, RoutineContract routineContract, LocalDateTime contractDate, String address, String subAddress, Double latitude, Double longitude, int totalPrice, int totalTime, boolean personal, ContractStatus status) {
        this.customer = customer;
        this.manager = manager;
        this.cleaning = cleaning;
        this.routineContract = routineContract;
        this.contractDate = contractDate;
        this.address = address;
        this.subAddress = subAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.totalPrice = totalPrice;
        this.totalTime = totalTime;
        this.personal = personal;
        this.status = ContractStatus.작업전;
    }

    public void updateDate(LocalDateTime date) {
        this.contractDate = date;
    }

    public void updateManager(Manager manager) {
        this.manager = manager;

    }
}
