package com.kdev5.cleanpick.manager.service.dto.response;

import lombok.Getter;

@Getter
public class ReadManagerResponseDto {
    Long managerId;
    String managerName;
    Double rating;
    Long reviewCnt;
    String profileImageUrl;
    String profileMessage;

    public ReadManagerResponseDto(Long managerId, String managerName, Double rating, Long reviewCnt, String profileImageUrl, String profileMessage) {
        this.managerId = managerId;
        this.managerName = managerName;
        this.rating = rating;
        this.reviewCnt = reviewCnt;
        this.profileImageUrl = profileImageUrl;
        this.profileMessage = profileMessage;
    }

}
