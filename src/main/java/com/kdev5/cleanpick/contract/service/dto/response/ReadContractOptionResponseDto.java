package com.kdev5.cleanpick.contract.service.dto.response;

import lombok.Builder;

@Builder
public record ReadContractOptionResponseDto(String name, String type) {
}