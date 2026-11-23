package com.demo.notification.listener;
import com.demo.notification.dto.NotificationRequest;
import com.demo.notification.entity.Notification;
import com.demo.notification.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component @Profile("!stub")
public class StockEventListener {
    private final NotificationService service;
    public StockEventListener(NotificationService service){this.service=service;}
    @RabbitListener(queues="notification.events")
    public void receive(NotificationRequest event){
        Notification n=new Notification();
        n.setUserId(event.userId()==null?1L:event.userId());
        n.setSku(event.sku());n.setMessage(event.message());
        n.setType(event.type()==null?"STOCK":event.type());
        n.setTitle(event.title());n.setSeverity(event.severity());n.setLink(event.link());
        service.create(n);
    }
}
