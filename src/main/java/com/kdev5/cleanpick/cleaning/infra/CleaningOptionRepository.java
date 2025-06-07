package com.kdev5.cleanpick.cleaning.infra;

import java.util.List;

import com.kdev5.cleanpick.cleaning.domain.CleaningOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CleaningOptionRepository extends JpaRepository<CleaningOption, Long> {

	List<CleaningOption> findCleaningOptionByCleaningId(final Long cleaningId);
}
