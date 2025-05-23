package com.kdev5.cleanpick.contract.domain;

import com.kdev5.cleanpick.cleaning.domain.CleaningOption;
import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(
        name = "contract_option",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"contract_id", "cleaning_option_id"})
        }
)
public class ContractOption extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @ManyToOne
    @JoinColumn(name = "cleaning_option_id", nullable = false)
    private CleaningOption cleaningOption;
}
