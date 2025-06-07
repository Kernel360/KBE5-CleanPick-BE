package com.kdev5.cleanpick.cleaning.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kdev5.cleanpick.cleaning.domain.CleaningOption;
import com.kdev5.cleanpick.cleaning.infra.CleaningOptionRepository;
import com.kdev5.cleanpick.cleaning.service.dto.response.CleaningOptionResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CleaningOptionService {

	private final CleaningOptionRepository cleaningOptionRepository;

	public List<CleaningOptionResponseDto> getCleaningOption(final Long cleaningId) {

		final List<CleaningOption> cleaningOptions =
			cleaningOptionRepository.findCleaningOptionByCleaningId(cleaningId);

		return cleaningOptions.stream().map(CleaningOptionResponseDto::fromEntity).toList();
	}
}
