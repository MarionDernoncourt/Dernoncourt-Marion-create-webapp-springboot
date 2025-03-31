package com.SafetyNet.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.SafetyNet.model.Firestation;
import com.SafetyNet.repository.IFirestationRepository;
import com.SafetyNet.repository.PersonRepository;

@Service
public class FirestationService {


	private static final Logger logger = LoggerFactory.getLogger(FirestationService.class);

	private IFirestationRepository firestationRepository;

	public FirestationService(IFirestationRepository firestationRepository) {
		this.firestationRepository = firestationRepository;
	}

	public List<Firestation> getAllFirestation() {
		logger.debug("Tentative de récupération de toutes les casernes");
		try {
			List<Firestation> stations = firestationRepository.getAllFirestation();

			if (stations.isEmpty()) {
				logger.error("Aucune caserne trouvée");
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune caserne trouvée");
			}
			logger.info("Nombre de casernes récupérées : {}", stations.size());
			return stations;
		} catch (IOException e) {
			logger.error("Erreur lors de la récupération des casernes", e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Erreur lors de la récupération des casernes");
		}
	}

	public Firestation createFirestation(Firestation firestation) {
		logger.debug("Tentative de création de la caserne : {}", firestation);
		
		List<Firestation> stations = getAllFirestation();
		for (Firestation station : stations) {
			if (station == firestation) {
				logger.error("Echec de la création : caserne déjà existante : {}", firestation);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Une caserne existe déjà a cette adresse");
			}
		}
		if((firestation.getAddress() == null || firestation.getAddress() == "") || firestation.getStation() == 0) {
			logger.error("Echec de la création : merci d'entrer une adresse et un numéro de station valide");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Une caserne existe déjà a cette adresse");

		}
		Firestation station = firestationRepository.createFirestation(firestation);
		logger.info("Caserne créée avec succès : {}", firestation);
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

		if (deleted) {
			logger.info("Caserne supprimée avec succès");
		} else {
			logger.error("Echec de la suppression, caserne non trouvée");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Impossible de supprimer, caserne non trouvée");
		}
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
		return getAllFirestation().stream()
				.filter(station -> station.getStation() == stationNumber).collect(Collectors.toList());
	}

}
