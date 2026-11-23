package com.demo.notification.entity;
import jakarta.persistence.*; import java.time.Instant;

@Entity @Table(name="notifications")
public class Notification {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private Long userId = 1L;
    private String sku;
    @Column(length=2000) private String message;
    private String type;
    private String title;
    private String severity;
    private String link;
    private boolean delivered;
    private boolean read;
    private Instant createdAt=Instant.now();
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Long getUserId(){return userId;} public void setUserId(Long userId){this.userId=userId;}
    public String getSku(){return sku;} public void setSku(String sku){this.sku=sku;}
    public String getMessage(){return message;} public void setMessage(String message){this.message=message;}
    public String getType(){return type;} public void setType(String type){this.type=type;}
    public String getTitle(){return title;} public void setTitle(String title){this.title=title;}
    public String getSeverity(){return severity;} public void setSeverity(String severity){this.severity=severity;}
    public String getLink(){return link;} public void setLink(String link){this.link=link;}
    public boolean isDelivered(){return delivered;} public void setDelivered(boolean delivered){this.delivered=delivered;}
    public boolean isRead(){return read;} public void setRead(boolean read){this.read=read;}
    public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant v){createdAt=v;}
}
