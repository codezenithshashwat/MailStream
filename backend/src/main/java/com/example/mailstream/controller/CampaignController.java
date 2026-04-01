package com.example.mailstream.controller;

import com.example.mailstream.model.Campaign;
import com.example.mailstream.repository.CampaignRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignRepository campaignRepository;

    public CampaignController(CampaignRepository campaignRepository){
        this.campaignRepository = campaignRepository;
    }

    @PostMapping
    public ResponseEntity<String> createCampaign(@RequestBody Campaign campaign){ // response entity here represents the entire HTTP response (status codes, header, body...standard REST practice)
        campaignRepository.save(campaign);

        return ResponseEntity.accepted().body("Campaign created successfully: ID of the campaign is: "+ campaign.getId());
    }

}
