package com.salonai.notification.service;

import com.salonai.booking.entity.Booking;
import com.salonai.notification.entity.Notification;
import com.salonai.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification bookingCreated(Booking booking) {
        Notification notification = new Notification();
        notification.setSalon(booking.getSalon());
        notification.setBooking(booking);
        notification.setTitle("New booking confirmed");
        notification.setMessage(
                booking.getCustomerName() + " booked " + booking.getService()
                        + " at " + booking.getBookingTime()
        );
        return notificationRepository.save(notification);
    }

    public List<Notification> latest(Long salonId) {
        if (salonId == null) {
            return notificationRepository.findTop20ByOrderByCreatedAtDesc();
        }
        return notificationRepository.findTop20BySalonIdOrderByCreatedAtDesc(salonId);
    }
}
