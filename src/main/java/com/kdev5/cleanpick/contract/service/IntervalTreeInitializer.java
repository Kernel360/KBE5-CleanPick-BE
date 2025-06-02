package com.kdev5.cleanpick.contract.service;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.infra.ContractRepository;
import com.kdev5.cleanpick.contract.service.support.IntervalTree;
import com.kdev5.cleanpick.contract.service.support.TimeInterval;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class IntervalTreeInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final ContractRepository contractRepository;
    private final IntervalTree intervalTree;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        List<Contract> contracts = contractRepository.findContractsFrom(startOfToday);

        for (Contract contract : contracts) {
            if (contract.getManager().getId() == null) continue;

            LocalDateTime start = contract.getContractDate();
            LocalDateTime end = start.plusHours(contract.getTotalTime());
            intervalTree.addReservation(contract.getManager().getId(), new TimeInterval(start, end));
        }

        log.info("✅ intervalTree 초기화 완료. 등록된 계약 수: {}", contracts.size());
    }

}
