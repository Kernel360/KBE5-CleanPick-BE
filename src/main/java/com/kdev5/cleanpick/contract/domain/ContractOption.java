package com.kdev5.cleanpick.contract.domain;

import com.kdev5.cleanpick.cleaning.domain.CleaningOption;
import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(
        name = "contract_option",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"contract_id", "option_id"})
        }
)
public class ContractOption extends BaseTimeEntity {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @ManyToOne
    @JoinColumn(name = "cleaning_option_id", nullable = false)
    private CleaningOption cleaningOption;
}
