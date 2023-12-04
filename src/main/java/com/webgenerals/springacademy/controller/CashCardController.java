package com.webgenerals.springacademy.controller;

import com.webgenerals.springacademy.domain.CashCard;
import com.webgenerals.springacademy.repository.CashCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {

	@Autowired
	private CashCardRepository cashCardRepository;

	@GetMapping
	public ResponseEntity<List<CashCard>> findAll(Pageable pageable, Principal principal) {
		Page<CashCard> page = cashCardRepository.findByOwner(
				principal.getName(),
				PageRequest.of(
						pageable.getPageNumber(),
						pageable.getPageSize(),
						pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
				));
		return ResponseEntity.ok(page.getContent());
	}

	@GetMapping("/{requestedId}")
	public ResponseEntity<CashCard> findById(@PathVariable Long requestedId, Principal principal) {
		Optional<CashCard> cashCardOptional = Optional.ofNullable(cashCardRepository.findByIdAndOwner(requestedId, principal.getName()));

		if (cashCardOptional.isPresent()) {
			return ResponseEntity.ok(cashCardOptional.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	private ResponseEntity<Void> createCashCard(@RequestBody CashCard newCashCardRequest, UriComponentsBuilder ucb, Principal principal) {
		CashCard cashCardWithOwner = new CashCard(null, newCashCardRequest.getAmount(), principal.getName());
		CashCard savedCashCard = cashCardRepository.save(cashCardWithOwner);
		URI locationOfNewCashCard = ucb.path("cashcards/{id}").buildAndExpand(savedCashCard.getId()).toUri();

		return ResponseEntity.created(locationOfNewCashCard).build();
	}

}
