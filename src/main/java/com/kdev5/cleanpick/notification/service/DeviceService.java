package com.kdev5.cleanpick.notification.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.kdev5.cleanpick.notification.domain.Device;
import com.kdev5.cleanpick.notification.service.dto.request.DeviceRegisterRequestDto;
import com.kdev5.cleanpick.notification.infra.DeviceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceService {

	private final DeviceRepository deviceRepository;

	public void registerOrUpdateDevice(DeviceRegisterRequestDto deviceRegisterRequestDto, Long userId) {
		Optional<Device> deviceOptional = deviceRepository.findByDeviceToken(deviceRegisterRequestDto.getDeviceToken());

		Device device;

		if(deviceOptional.isPresent()) {
			device = deviceOptional.get();
			device.renewDeviceInfo(
				userId,
				deviceRegisterRequestDto.getDeviceType()
			);
		} else {
			device = deviceRepository.save(deviceRegisterRequestDto.toEntity(userId));
		}
	}



	private void sendMessage(String token, String title, String body) {
		Notification notification = Notification.builder()
			.setTitle(title)
			.setBody(body)
			.build();

		Message message = Message.builder()
			.setToken(token)
			.setNotification(notification)
			.build();

		try {
			String response = FirebaseMessaging.getInstance().send(message);
			System.out.println("Successfully sent message: " + response);
		} catch (Exception e) {
			System.out.println("Failed to send message: " + e.getMessage());
		}
	}
}
