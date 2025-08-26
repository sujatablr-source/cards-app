/**
 * 
 */
package com.vs3.card.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vs3.card.entity.Card;
import com.vs3.card.model.CardRequest;
import com.vs3.card.model.CardResponse;
import com.vs3.card.repository.CardRepository;
import com.vs3.card.service.CardService;
import com.vs3.card.utils.CryptoUtils;
import com.vs3.card.utils.PanHasher;

/**
 * 
 */
@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository repo;

    public CardResponse createCard(CardRequest req) throws Exception {
    	String pan = req.getCardNumber();
    	String cardholderName = req.getCardHolderName();
   /*     if (!PanValidator.isValidPan(pan)) {
            throw new IllegalArgumentException("Invalid PAN");
        }
*/
        String iv = CryptoUtils.randomIV();
        String encrypted = CryptoUtils.encrypt(pan, iv);
        String panIndex = PanHasher.hashPan(pan);
        String last4 = pan.substring(pan.length() - 4);

        Card card = new Card();
        card.setCardholderName(cardholderName);
        card.setEncryptedPan(encrypted);
        card.setIv(iv);
        card.setPanIndex(panIndex);
        card.setLast4(last4);
        Card res = repo.save(card);

        /*CardResponse.builder()
                .id(saved.getId())
                .cardholderName(saved.getCardholderName())
                .maskedPan(maskPan(saved.getLast4()))
                .createdAt(saved.getCreatedAt())
                .build();
                */
        CardResponse response = new CardResponse();
        response.setCardHolderName(res.getCardholderName());
        response.setCardNumber(maskPan(res.getLast4()));
        response.setCreatedDate(res.getCreatedAt());
        return response;
    }

    public List<CardResponse> searchByFullPan(String pan) throws Exception {
        List<Card> res = repo.findByPanIndex(pan);
        System.out.println("searchByFullPan  "+res);
        return res.stream().map(c -> {
            CardResponse card = new CardResponse();
        	card.setCardHolderName(c.getCardholderName());
        	card.setCardNumber(maskPan(c.getLast4()));
        	card.setCreatedDate(c.getCreatedAt());
        	return card;
        }).toList();
    }

    public List<CardResponse> searchByLast4(String last4) {
        List<Card> res = repo.findByLast4(last4);
        return res.stream().map(c -> {
            CardResponse card = new CardResponse();
        	card.setCardHolderName(c.getCardholderName());
        	card.setCardNumber(maskPan(c.getLast4()));
        	card.setCreatedDate(c.getCreatedAt());
        	return card;
        }).toList();
        
    }
    
    private String maskPan(String last4) {
        return "************" + last4;
    }
}
