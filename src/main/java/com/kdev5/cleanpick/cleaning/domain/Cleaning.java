package com.kdev5.cleanpick.cleaning.domain;

import com.kdev5.cleanpick.cleaning.domain.enumeration.ServiceName;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceName serviceName;

    @Lob
    private String content;

    @Builder
    public Cleaning(ServiceName serviceName, String content) {
        this.serviceName = serviceName;
        this.content = content;
    }

    public static Cleaning reference(Long id){
        return new Cleaning(id);
    }

    private Cleaning(Long id) {
        this.id = id;
    }
}