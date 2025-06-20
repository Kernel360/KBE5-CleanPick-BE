package com.kdev5.cleanpick.contract.domain;

import com.kdev5.cleanpick.converter.DayOfWeekListConverter;
import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "routine_contract")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "deleted_at IS NULL")
public class RoutineContract extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discount_rate", nullable = false)
    private float discountRate;

    @Column(name = "contract_start_date", nullable = false)
    private LocalDateTime contractStartDate;

    @Column(name = "routine_count", nullable = false)
    private int routineCount;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @Column(name = "day_of_week", nullable = false)
    @Convert(converter = DayOfWeekListConverter.class)
    private List<DayOfWeek> dayOfWeek;


    @Builder
    public RoutineContract(float discountRate, LocalDateTime contractStartDate, int routineCount, LocalDateTime startTime, List<DayOfWeek> dayOfWeek, LocalDateTime time) {
        this.discountRate = discountRate;
        this.contractStartDate = contractStartDate;
        this.routineCount = routineCount;
        this.startTime = startTime;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
    }

}