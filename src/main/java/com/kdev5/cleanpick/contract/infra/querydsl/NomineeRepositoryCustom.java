package com.kdev5.cleanpick.contract.infra.querydsl;

import com.kdev5.cleanpick.cleaning.domain.enumeration.ServiceName;
import com.kdev5.cleanpick.contract.domain.Nominee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NomineeRepositoryCustom {
    Page<Nominee> findRequestedMatching(Long managerId, Boolean isPersonal, Pageable pageable);

    Page<Nominee> findRequestMatching(Long managerId, ServiceName type, Pageable pageable);
}
