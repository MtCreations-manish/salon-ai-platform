package com.salonai.notification.repository;

import com.salonai.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findTop20BySalonIdOrderByCreatedAtDesc(Long salonId);

    List<Notification> findTop20ByOrderByCreatedAtDesc();
}
