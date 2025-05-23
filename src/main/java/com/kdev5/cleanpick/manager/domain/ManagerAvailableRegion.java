package com.kdev5.cleanpick.manager.domain;

import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import com.kdev5.cleanpick.manager.domain.Region;
import jakarta.persistence.*;

@Entity
@Table(name = "manager_available_region")
public class ManagerAvailableRegion extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "region_id", insertable = false, updatable = false)
    private Region region;

    @ManyToOne
    @JoinColumn(name = "manager_id", insertable = false, updatable = false)
    private Manager manager;
}
