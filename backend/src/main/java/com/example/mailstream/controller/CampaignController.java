package com.example.mailstream.controller;

import com.example.mailstream.model.Campaign;
import com.example.mailstream.repository.CampaignRepository;
import com.example.mailstream.service.KafkaProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignRepository campaignRepository;
    private final KafkaProducerService kafkaProducerService;

    public CampaignController(CampaignRepository campaignRepository, KafkaProducerService kafkaProducerService){

        this.campaignRepository = campaignRepository;
        this.kafkaProducerService= kafkaProducerService;
    }

    @PostMapping
    public ResponseEntity<String> createCampaign(@RequestBody Campaign campaign){ // response entity here represents the entire HTTP response (status codes, header, body...standard REST practice)
//        campaignRepository.save(campaign);

        Campaign savedCampaign= campaignRepository.save(campaign);

        //publishing the event to kafka
        kafkaProducerService.sendCampaignEvent(savedCampaign.getId().toString());

        return ResponseEntity.accepted().body("Campaign created successfully: ID of the campaign is: "+ campaign.getId());
    }

}
