package com.demo.notification.service;
import com.demo.notification.entity.Notification;
import com.demo.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository repository;
    public NotificationService(NotificationRepository repository){this.repository=repository;}
    public Notification create(Notification n){n.setDelivered(true);return repository.save(n);}
    public List<Notification> findAll(){return repository.findAll();}
    public Notification findById(Long id){return repository.findById(id).orElseThrow();}
    public List<Notification> findForUser(Long userId, boolean unreadOnly) {
        return repository.findAll().stream()
                .filter(n -> userId.equals(n.getUserId()))
                .filter(n -> !unreadOnly || !n.isRead())
                .toList();
    }
    public Notification markRead(Long id) {
        Notification notification = findById(id);
        notification.setRead(true);
        return repository.save(notification);
    }
    public void markAllRead(Long userId) {
        findForUser(userId, true).forEach(notification -> {
            notification.setRead(true);
            repository.save(notification);
        });
    }
}
