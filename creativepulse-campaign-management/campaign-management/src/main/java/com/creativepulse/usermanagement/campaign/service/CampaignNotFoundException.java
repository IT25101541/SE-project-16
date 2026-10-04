package com.creativepulse.usermanagement.campaign.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Shows the 404 page when a campaign id does not exist. */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CampaignNotFoundException extends RuntimeException {

    public CampaignNotFoundException(Long id) {
        super("Campaign " + id + " not found");
    }
}
