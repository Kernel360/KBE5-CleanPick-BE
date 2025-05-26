package com.kdev5.cleanpick.cleaning.domain;

import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "cleaning")
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cleaning extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name", length = 255, nullable = false)
    private String serviceName;

    @Lob
    private String content;

    @Builder
    public Cleaning(String serviceName, String content) {
        this.serviceName = serviceName;
        this.content = content;
    }
}