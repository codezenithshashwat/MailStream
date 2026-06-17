package com.example.mailstream.controller;

import com.example.mailstream.model.Campaign;
import com.example.mailstream.repository.CampaignRepository;
import com.example.mailstream.service.KafkaProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/campaigns")
@CrossOrigin(origins = "http://localhost:4200") //for testing on apache jmeter
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

        Campaign savedCampaign= campaignRepository.save(campaign); //saving the campaign in the database

        //publishing the event to kafka instantly as campaign is created
        kafkaProducerService.sendCampaignEvent(savedCampaign.getId().toString());

        return ResponseEntity.accepted().body("Campaign created successfully: ID of the campaign is: "+ campaign.getId());
    }

}
