package com.example.mailstream.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
public final KafkaTemplate<String, String> kafkaTemplate;

public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate){
    this.kafkaTemplate= kafkaTemplate;
}
public void sendCampaignEvent(String campaignId){
    kafkaTemplate.send("campaign_event", campaignId);
    System.out.println("Producer Just Dropped campaign ID: "+ campaignId + " into Kafka.");

}
}
