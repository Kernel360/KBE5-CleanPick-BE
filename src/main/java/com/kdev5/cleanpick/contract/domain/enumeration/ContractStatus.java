package com.kdev5.cleanpick.contract.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContractStatus {
//    작업전, 작업중, 작업후, 정산전, 정산완료
    NOT_STARTED("작업전"),
    IN_PROGRESS("작업중"),
    COMPLETED("작업후"),
    AWATING_PAYMENT("정산전"),
    PAID("정산완료");

    private final String description;
}