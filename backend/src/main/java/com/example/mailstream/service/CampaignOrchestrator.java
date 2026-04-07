package com.example.mailstream.service;

import com.example.mailstream.model.Campaign;
import com.example.mailstream.model.DeliveryLog;
import com.example.mailstream.model.Subscriber;
import com.example.mailstream.repository.CampaignRepository;
import com.example.mailstream.repository.DeliveryLogRepository;
import com.example.mailstream.repository.SubscriberRepository;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CampaignOrchestrator {

    private final CampaignRepository campaignRepository;
    private final SubscriberRepository subscriberRepository;
    private final DeliveryLogRepository deliveryLogRepository;
    private final JavaMailSender mailSender;  //spring will automatically link this to mailpit

    public CampaignOrchestrator(CampaignRepository campaignRepository, SubscriberRepository subscriberRepository, DeliveryLogRepository deliveryLogRepository, JavaMailSender mailSender){
        this.campaignRepository = campaignRepository;
        this.subscriberRepository = subscriberRepository;
        this.deliveryLogRepository = deliveryLogRepository;
        this.mailSender = mailSender;
    }

    // kafkalistener annotation creates a thread in the background that constantly listens to the tpoic.
    @KafkaListener(topics= "campaign_event", groupId= "mailstream-group")
    public void listenForNewCampaigns(String campaignIdStr){
        System.out.println("CONSUMER (Orchestrator): Just received Campaign ID [" + campaignIdStr + "] from Kafka!");
        System.out.println("️CONSUMER (Orchestrator): I will now start fetching subscribers from the database in the background...");

        try{
            UUID campaignId=  UUID.fromString(campaignIdStr);

            Campaign campaign= campaignRepository.findById(campaignId).orElseThrow(() -> new RuntimeException("Campaign not found"));

            List<Subscriber> subscribers= subscriberRepository.findAll();

            if(subscribers.isEmpty()){
                System.out.println("No subscrivers found");
                return;
            }

            for(Subscriber subscriber : subscribers){

                try{
                   SimpleMailMessage message = new SimpleMailMessage();
                   message.setFrom("hello@mailstream.com");
                   message.setTo(subscriber.getEmail());
                   message.setSubject(campaign.getSubject());
                   message.setText(campaign.getBodyContent());

                   mailSender.send(message);

                   DeliveryLog log= new DeliveryLog();
                   log.setCampaign(campaign);
                   log.setSubscriber(subscriber);
                   log.setStatus("sent");
                   log.setSentAT(LocalDateTime.now());
                   deliveryLogRepository.save(log);

                   System.out.println("Email Sent Successfully to: "+ subscriber.getEmail());
                }
                catch (Exception e){
                    System.err.println("Failed to sent email to: " + subscriber.getEmail());
                }
            }
            System.out.println("Campaign processed successfully");

        }
        catch(Exception e){
            System.out.println("Error" + e.getMessage());
        }


    }
}
