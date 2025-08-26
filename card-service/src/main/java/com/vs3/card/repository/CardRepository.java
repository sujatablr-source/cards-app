package com.vs3.card.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vs3.card.entity.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByPanIndex(String panIndex);
    
    List<Card> findByLast4(String last4);
}

