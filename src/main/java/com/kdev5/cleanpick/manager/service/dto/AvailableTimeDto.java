package com.kdev5.cleanpick.manager.service.dto;

import java.time.LocalTime;

import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.manager.domain.ManagerAvailableTime;
import com.kdev5.cleanpick.manager.domain.enumeration.DayOfWeek;

import lombok.Getter;

@Getter
public class AvailableTimeDto {

	private DayOfWeek dayOfWeek;

	private LocalTime startTime;

	private LocalTime endTime;

	public ManagerAvailableTime toManagerAvailableTime(Manager manager) {
		return ManagerAvailableTime.builder()
			.manager(manager)
			.dayOfWeek(dayOfWeek)
			.startTime(startTime)
			.endTime(endTime)
			.build();
	}

}