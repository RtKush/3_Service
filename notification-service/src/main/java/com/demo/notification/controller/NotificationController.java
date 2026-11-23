package com.demo.notification.controller;
import com.demo.notification.dto.NotificationRequest;
import com.demo.notification.entity.Notification;
import com.demo.notification.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@RestController
public class NotificationController {
    private final NotificationService service;
    public NotificationController(NotificationService service){this.service=service;}
    @GetMapping({"/api/notifications","/api/notifications/all","/api/v1/notifications/legacy"})
    public List<Notification> all(){return service.findAll();}
    @GetMapping("/api/notifications/{id}")
    public Notification byId(@PathVariable Long id){return service.findById(id);}
    @PostMapping({"/api/v1/notifications","/api/notifications"})
    public ResponseEntity<Notification> create(@Valid @RequestBody NotificationRequest r){
        Notification n=new Notification();
        n.setUserId(r.userId()==null?1L:r.userId());
        n.setSku(r.sku());n.setMessage(r.message());n.setType(r.type()==null?"GENERAL":r.type());
        n.setTitle(r.title());n.setSeverity(r.severity());n.setLink(r.link());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(n));
    }
    @PostMapping("/api/notifications/send")
    public ResponseEntity<Notification> send(@Valid @RequestBody NotificationRequest r){return create(r);}

    @GetMapping("/api/v1/notifications")
    public List<Notification> userNotifications(
            @RequestHeader(value = "X-User-Id", defaultValue = "1") Long userId,
            @RequestParam(defaultValue = "false") boolean unreadOnly) {
        return service.findForUser(userId, unreadOnly);
    }

    @GetMapping("/api/v1/notifications/unread-count")
    public Map<String, Long> unreadCount(
            @RequestHeader(value = "X-User-Id", defaultValue = "1") Long userId) {
        return Map.of("count", (long) service.findForUser(userId, true).size());
    }

    @PostMapping("/api/v1/notifications/{id}/read")
    public Notification markRead(@PathVariable Long id) {
        return service.markRead(id);
    }

    @PostMapping("/api/v1/notifications/read-all")
    public ResponseEntity<Void> markAllRead(
            @RequestHeader(value = "X-User-Id", defaultValue = "1") Long userId) {
        service.markAllRead(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/v1/notifications/report")
    public Map<String,Object> report(
            @RequestHeader(value="X-User-Id",defaultValue="1") Long userId) {
        return Map.of("asOf",Instant.now(),"notifications",
            service.findForUser(userId,false),"unreadCount",
            service.findForUser(userId,true).size());
    }

    @GetMapping(value="/api/v1/notifications/export/csv",produces="text/csv")
    public ResponseEntity<byte[]> exportCsv(
            @RequestHeader(value="X-User-Id",defaultValue="1") Long userId) {
        StringBuilder csv=new StringBuilder("id,userId,type,title,severity,message,read,createdAt\n");
        for(Notification n:service.findForUser(userId,false)){
            csv.append(n.getId()).append(',').append(n.getUserId()).append(',')
               .append(n.getType()).append(',').append(safe(n.getTitle())).append(',')
               .append(safe(n.getSeverity())).append(',').append(safe(n.getMessage()).replace(","," "))
               .append(',').append(n.isRead()).append(',').append(n.getCreatedAt()).append('\n');
        }
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=notifications.csv")
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(csv.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static String safe(String value){return value==null?"":value.replace("\n"," ");}
}
