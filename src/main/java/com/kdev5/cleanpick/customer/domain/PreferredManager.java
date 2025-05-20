package com.kdev5.cleanpick.customer.domain;
import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import com.kdev5.cleanpick.user.domain.PreferredManagerId;
import com.kdev5.cleanpick.user.domain.User;
import jakarta.persistence.*;
import org.apache.catalina.Manager;

@Entity
@Table(name = "preferred_manager")
public class PreferredManager extends BaseTimeEntity {

    @EmbeddedId
    private PreferredManagerId id;

    @MapsId("customerId")
    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @MapsId("managerId")
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;
}

