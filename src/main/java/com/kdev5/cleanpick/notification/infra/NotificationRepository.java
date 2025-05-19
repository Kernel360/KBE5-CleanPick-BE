package com.kdev5.cleanpick.notification.infra;

import com.kdev5.cleanpick.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
