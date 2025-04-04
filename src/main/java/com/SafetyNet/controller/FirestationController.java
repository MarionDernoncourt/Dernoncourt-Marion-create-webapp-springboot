package com.SafetyNet.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.model.Firestation;
import com.SafetyNet.service.FirestationService;

@RestController
public class FirestationController {

	private static final Logger logger = LoggerFactory.getLogger(FirestationService.class);

	@Autowired
	private FirestationService firestationService;

	@GetMapping("/firestations")
	public ResponseEntity<List<Firestation>> getAllFirestation() {
		try {
			logger.debug("Requete GET firestations reçue");
			List<Firestation> firestations = firestationService.getAllFirestation();
			if (firestations.isEmpty()) {
				logger.error("Réponse reçue : 404 NOT_FOUND, aucune caserne trouvée");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			logger.info("Réponse reçue : 200 OK, nombre de casernes trouvées : {}", firestations.size());
			return ResponseEntity.status(HttpStatus.OK).body(firestations);
		} catch (Exception e) {
			logger.error("Erreur lors de la récupération des casernes : {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/firestation")
	public ResponseEntity<Firestation> createFirestation(@RequestBody Firestation firestation) {
		try {
			logger.debug("Requete CREATE firestation reçue");
			Firestation firestationCreated = firestationService.createFirestation(firestation);
			if (firestationCreated == null) {
				logger.error("Réponse 409 CONFLICT, une caserne existe déjà avec ces informations : {}", firestation);
				return ResponseEntity.status(HttpStatus.CONFLICT).build();
			}
			logger.info("Réponse 201 CREATED, la caserne {} {} a été créée avec succès", firestation.getAddress(), firestation.getStation());
			return ResponseEntity.status(HttpStatus.CREATED).body(firestationCreated);

		} catch (Exception e) {
			logger.error("Erreur lors de la création de la caserne : {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/firestation")
	public ResponseEntity<Firestation> updateFirestation(@RequestBody Firestation firestation) {
		try {
			logger.debug("Requete reçue UPDATE firestation");
			Firestation updatedFirestation = firestationService.updateFirestation(firestation);
			if (updatedFirestation == null) {
				logger.error("Réponse reçue : 404 NOT FOUND, la caserne {}, non trouvée", firestation);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			logger.info("Réponse reçue 200 OK, la caserne a été modifiée : {} {}", firestation.getAddress(), firestation.getStation());
			return ResponseEntity.status(HttpStatus.OK).body(updatedFirestation);
		} catch (Exception e) {
			logger.error("Erreur lors de la mise à jour de la caserne : {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/firestation")
	public ResponseEntity<Void> deleteFirestation(@RequestParam(required = false) Optional<String> address,
			@RequestParam(required = false) Optional<Integer> stationNumber) {
		logger.debug("Requete DELETE {} {}", address, stationNumber);
		try {
			if (!address.isPresent() && !stationNumber.isPresent()) {
				logger.error("Aucun paramètre fourni pour la suppression");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			boolean deleted = firestationService.deleteFirestation(address.orElse(null), stationNumber.orElse(null));
			if (!deleted) {
				logger.error("Aucune caserne trouvée avec ces informations : {} {}", address, stationNumber);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			logger.info("Caserne supprimée avec succès");
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

		} catch (Exception e) {
			logger.error("Erreur lors de la suppression de la caserne : {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
