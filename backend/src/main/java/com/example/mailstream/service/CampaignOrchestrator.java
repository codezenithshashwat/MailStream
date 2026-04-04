package com.example.mailstream.service;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CampaignOrchestrator {

    // kafkalistener annotation creates a thread in the background that constantly listens to the tpoic.
    @KafkaListener(topics= "campaign-initiation", groupId= "mailstream-group")
    public void listenForNewCampaigns(String campaignId){
        System.out.println("CONSUMER (Orchestrator): Just received Campaign ID [" + campaignId + "] from Kafka!");
        System.out.println("️CONSUMER (Orchestrator): I will now start fetching subscribers from the database in the background...");

        //fetching code to be written later
    }
}
