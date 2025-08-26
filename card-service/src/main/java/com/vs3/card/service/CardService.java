package com.vs3.card.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vs3.card.entity.Card;
import com.vs3.card.model.CardRequest;
import com.vs3.card.model.CardResponse;

/**
 * 
 */
@Service
public interface CardService {
	
	
	CardResponse createCard(CardRequest req) throws Exception;
	
//	Optional<Card> searchByFullPan(String pan) throws Exception;
	List<CardResponse> searchByFullPan(String pan) throws Exception;
	
	List<CardResponse> searchByLast4(String last4);

}
