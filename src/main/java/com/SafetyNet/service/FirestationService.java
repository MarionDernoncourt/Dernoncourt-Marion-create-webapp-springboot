package com.SafetyNet.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.SafetyNet.model.Firestation;
import com.SafetyNet.repository.IFirestationRepository;

@Service
public class FirestationService {

	private static final Logger logger = LoggerFactory.getLogger(FirestationService.class);

	private IFirestationRepository firestationRepository;

	public FirestationService(IFirestationRepository firestationRepository) {
		this.firestationRepository = firestationRepository;
	}

	public List<Firestation> getAllFirestation() {
		logger.debug("Tentative de récupération de toutes les casernes");

		List<Firestation> stations = firestationRepository.getAllFirestation();

		return stations;

	}

	public Firestation createFirestation(Firestation firestation) {
		logger.debug("Tentative de création de la caserne : {}", firestation);

		List<Firestation> stations = getAllFirestation();
		for (Firestation station : stations) {
			if (station == firestation) {
				logger.error("Echec de la création : caserne déjà existante : {}", firestation);
				return null;
			}
		}
		if ((firestation.getAddress() == null || firestation.getAddress() == "") || firestation.getStation() == 0) {
			logger.error("Echec de la création : merci d'entrer une adresse et un numéro de station valide");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Une caserne existe déjà a cette adresse");

		}
		Firestation station = firestationRepository.createFirestation(firestation);

		return station;
	}

	public Firestation updateFirestation(Firestation firestation) {
		logger.debug("Tentative de mise à jour de la caserne : {}", firestation);
		Firestation updated = firestationRepository.updateFirestation(firestation);
		if (updated == null) {
			logger.error("Echec de la mise à jour, caserne introuvable : {}", firestation);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Caserne non trouvée pour la mise à jour");
		}
		return updated;

	}

	public boolean deleteFirestation(String address, Integer stationNumber) {
		logger.debug("Tentative de suppression de la caserne : {} {}", address, stationNumber);

		boolean deleted = firestationRepository.deleteFirestation(address, stationNumber);

		return deleted;
	}

	public Firestation findByAddress(String address) {
		logger.debug("Accès aux données, rechercher de la caserne à l'adresse : {}", address);
		Firestation stationByAddress = getAllFirestation().stream()
				.filter(station -> station.getAddress().equalsIgnoreCase(address)).findFirst().orElse(null);
		return stationByAddress;
	}

	public List<Firestation> findByStationNumber(int stationNumber) {
		logger.debug("Accès aux données, recherche des casernes rattachées au numéro : {}", stationNumber);
		return getAllFirestation().stream().filter(station -> station.getStation() == stationNumber)
				.collect(Collectors.toList());
	}

}
