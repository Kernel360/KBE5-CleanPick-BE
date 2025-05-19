package com.kdev5.cleanpick.cleaning.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kdev5.cleanpick.cleaning.domain.Cleaning;

public interface CleaningRepository extends JpaRepository<Cleaning, Long> {
}
