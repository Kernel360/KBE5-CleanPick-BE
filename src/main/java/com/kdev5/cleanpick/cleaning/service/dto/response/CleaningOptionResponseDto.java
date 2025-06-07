package com.kdev5.cleanpick.cleaning.service.dto.response;

import com.kdev5.cleanpick.cleaning.domain.CleaningOption;

import lombok.Getter;

@Getter
public class CleaningOptionResponseDto {

	private final Long id;

	private final String type;

	private final String name;

	private final int extraPrice;

	private final int extraDuration;

	public CleaningOptionResponseDto(Long id, String type, String name, int extraPrice, int extraDuration) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.extraPrice = extraPrice;
		this.extraDuration = extraDuration;
	}

	public static CleaningOptionResponseDto fromEntity(final CleaningOption cleaningOption) {
		return new CleaningOptionResponseDto(
			cleaningOption.getId(),
			cleaningOption.getType(),
			cleaningOption.getName(),
			cleaningOption.getExtraPrice(),
			cleaningOption.getExtraDuration()
		);
	}
}
