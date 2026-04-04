package com.example.mailstream.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
public final KafkaTemplate<String, String> kafkaTemplate; //KafkaTemplate is a powerful buildin spring object

public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate){
    this.kafkaTemplate= kafkaTemplate;
}
public void sendCampaignEvent(String campaignId){
    kafkaTemplate.send("campaign_event", campaignId);
    //.send() initiates heavy lifting
    // 1. opens tcp socket on docker on port 9092
    // 2. takes capaign id and serializes it to raw byte array
    // 3. pushes the byte array tot he end of the partition for the topic "campaign initiation"

    System.out.println("Producer Just Dropped campaign ID: "+ campaignId + " into Kafka.");

}
}
