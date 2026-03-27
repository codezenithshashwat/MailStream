package com.example.mailstream.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name= "delivery_logs")
public class DeliveryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="campaign_id")
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name="subscriber_id")
    private Subscriber subscriber;

    private String status= "QUEUED"; //can be queued, sent, failed
    private LocalDateTime sentAT = LocalDateTime.now();

    public DeliveryLog() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getSentAT() {
        return sentAT;
    }

    public void setSentAT(LocalDateTime sentAT) {
        this.sentAT = sentAT;
    }
}
