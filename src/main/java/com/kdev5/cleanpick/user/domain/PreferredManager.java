package com.kdev5.cleanpick.user.domain;

import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "preferred_manager")
public class PreferredManager extends BaseTimeEntity {

    @EmbeddedId
    private PreferredManagerId id;

    @MapsId("customerId")
    @OneToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @MapsId("managerId")
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;
}
