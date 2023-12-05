package com.webgenerals.cashcards.controller;

import com.webgenerals.cashcards.domain.CashCard;
import com.webgenerals.cashcards.repository.CashCardRepository;
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

@RestController
@RequestMapping("/cashcards")
public class CashCardController {

	@Autowired
	private CashCardRepository cashCardRepository;

	@DeleteMapping("/{id}")
	private ResponseEntity<Void> deleteCashCard(@PathVariable Long id, Principal principal) {
		if (cashCardRepository.existsByIdAndOwner(id, principal.getName())) {
			cashCardRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.notFound().build();
	}

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
	private ResponseEntity<CashCard> findById(@PathVariable Long requestedId, Principal principal) {
		CashCard cashCard = findCashCard(requestedId, principal);

		if (cashCard != null) {
			return ResponseEntity.ok(cashCard);
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

	@PutMapping("/{requestedId}")
	private ResponseEntity<Void> putCashCard(@PathVariable Long requestedId, @RequestBody CashCard cashCardUpdate, Principal principal) {
		CashCard cashCard = findCashCard(requestedId, principal);

		if (cashCard != null) {
			CashCard updatedCashCard = new CashCard(cashCard.getId(), cashCardUpdate.getAmount(), principal.getName());
			cashCardRepository.save(updatedCashCard);
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.notFound().build();
	}

	private CashCard findCashCard(Long requestedId, Principal principal) {
		return cashCardRepository.findByIdAndOwner(requestedId, principal.getName());
	}

}
