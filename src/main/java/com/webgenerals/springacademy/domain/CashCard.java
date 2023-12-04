package com.webgenerals.springacademy.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CashCard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	Double amount;

	String owner;
}
