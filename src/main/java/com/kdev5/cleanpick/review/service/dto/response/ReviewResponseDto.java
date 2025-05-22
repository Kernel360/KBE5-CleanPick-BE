package com.kdev5.cleanpick.review.service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ReviewResponseDto{
    private long id;
    private String targetName;
    private Float rating;
    private String content;
    private List<String> files;

    private ReviewResponseDto setFiles(List<String> files){
        this.files = files;
        return this;
    }
}