package com.kdev5.cleanpick.notification.infra;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdev5.cleanpick.notification.domain.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {

	Optional<Device> findByDeviceToken(String deviceToken);
}
