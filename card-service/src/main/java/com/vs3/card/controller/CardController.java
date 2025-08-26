package com.vs3.card.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vs3.card.model.CardRequest;
import com.vs3.card.model.CardResponse;
import com.vs3.card.service.CardService;

@RestController
@RequestMapping("/v1/cards")
@CrossOrigin(origins = "http://localhost:5173")
public class CardController {
	
	@Autowired
	CardService cardService;
	
	@PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createCard(@Validated @RequestBody CardRequest cardRequest) throws Exception {
		return ResponseEntity.ok(cardService.createCard(cardRequest));
	}

	@PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> search(@RequestParam(required = false) String pan,
            @RequestParam(required = false) String last4) throws Exception{
    	List<CardResponse> cards = new ArrayList<CardResponse>();
    	
    	if(pan == "" && last4 == "") {
    		return ResponseEntity.badRequest().body("Provide either pan or last4");
    	}
    	if (pan != "") {
    		cards = cardService.searchByFullPan(pan);
        } else if (last4 != "") {
        	cards = cardService.searchByLast4(last4);
        }
    	if(cards.size() > 0) {
    		return ResponseEntity.ok(cards);
    	}
    	else {
    		return ResponseEntity.status(0).body("No matching cards found.");
    	}
    	
	}
	
}
