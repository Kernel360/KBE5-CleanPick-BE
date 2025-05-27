package com.kdev5.cleanpick.contract.infra;

import com.kdev5.cleanpick.contract.domain.Nominee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NomineeRepository extends JpaRepository<Nominee, Long> {
}
