package com.vs3.card.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardholderName;

    // Encrypted PAN (Base64 encoded ciphertext)
    @Column(nullable = false, length = 512)
    private String encryptedPan;

    // Random IV per record (Base64)
    @Column(nullable = false, length = 64)
    private String iv;

    // Secure searchable index (HMAC-SHA-256 of PAN)
    @Column(nullable = false, unique = true, length = 128)
    private String panIndex;

    // Last 4 digits of card 
    @Column(nullable = false, length = 4)
    private String last4;

    private LocalDateTime createdAt = LocalDateTime.now();

	public String getCardholderName() {
		return cardholderName;
	}

	public void setCardholderName(String cardholderName) {
		this.cardholderName = cardholderName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEncryptedPan() {
		return encryptedPan;
	}

	public void setEncryptedPan(String encryptedPan) {
		this.encryptedPan = encryptedPan;
	}

	public String getIv() {
		return iv;
	}

	public void setIv(String iv) {
		this.iv = iv;
	}

	public String getPanIndex() {
		return panIndex;
	}

	public void setPanIndex(String panIndex) {
		this.panIndex = panIndex;
	}

	public String getLast4() {
		return last4;
	}

	public void setLast4(String last4) {
		this.last4 = last4;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
    
}
