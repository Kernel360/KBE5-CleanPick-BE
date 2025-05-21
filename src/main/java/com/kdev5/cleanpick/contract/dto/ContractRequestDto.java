package com.kdev5.cleanpick.contract.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ContractRequestDto {

    @JsonProperty("customer_id")
    private Long customerId;

    @JsonProperty("manager_id")
    private Long managerId; // nullable

    @JsonProperty("cleaning_id")
    private Long cleaningId;

    @JsonProperty("routine_contract_id")
    private Long routineContractId; // optional

    @JsonProperty("contract_date")
    private LocalDateTime contractDate;

    @JsonProperty("address")
    private String address;

    @JsonProperty("total_price")
    private int totalPrice;

    @JsonProperty("total_time")
    private int totalTime;

    @JsonProperty("is_personal")
    private boolean personal;

}
