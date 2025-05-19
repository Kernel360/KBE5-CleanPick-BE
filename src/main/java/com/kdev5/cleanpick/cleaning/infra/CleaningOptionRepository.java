package com.kdev5.cleanpick.cleaning.infra;

import com.kdev5.cleanpick.cleaning.domain.CleaningOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CleaningOptionRepository extends JpaRepository<CleaningOption, Long> {
}
