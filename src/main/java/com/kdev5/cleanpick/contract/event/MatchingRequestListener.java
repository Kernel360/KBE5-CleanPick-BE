package com.kdev5.cleanpick.contract.event;

import com.kdev5.cleanpick.contract.service.ContractMatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatchingRequestListener {

    private final ContractMatchingService contractMatchingService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMatching(MatchingRequestEvent event) {
        if (event.getContractDates() != null) {
            contractMatchingService.requestRoutineCleaning(
                    event.getContractId(),
                    event.getLatitude(),
                    event.getLongitude(),
                    event.getContractDates()
            );
        } else {
            contractMatchingService.requestCleaning(
                    event.getContractId(),
                    event.getLatitude(),
                    event.getLongitude(),
                    event.getStart(),
                    event.getEnd()
            );
        }
    }
}
