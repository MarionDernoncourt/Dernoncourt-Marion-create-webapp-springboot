package com.SafetyNet.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SafetyNet.model.Firestation;
import com.SafetyNet.repository.FirestationRepository;
import com.SafetyNet.repository.IFirestationRepository;
import com.SafetyNet.repository.PersonRepository;

import lombok.Data;

@Service
public class FirestationService {

	private static final Logger logger = LoggerFactory.getLogger(FirestationService.class);

	@Autowired
	private FirestationRepository firestationRepository;

	public List<Firestation> getAllFirestation() throws IOException {
		logger.debug("Tentative de récupération de toutes les casernes");
		List<Firestation> stations = firestationRepository.getAllFirestation();
		logger.info("Nombre de casernes récupérées : {}", stations.size());
		return stations;
	}

	public Firestation getFirestationByAddress(String address) {
		logger.debug("Tentative de récupération de caserne avec son adresse : {}", address);
		Firestation station = firestationRepository.getFirestationByAddress(address);
		if (station == null) {
			logger.error("Aucune caserne a cette adresse {}", address);
		}
		return station;
	}

	public Firestation createFirestation(Firestation firestation) {
		logger.debug("Tentative de création de la caserne : {}", firestation);
		Firestation station = firestationRepository.createFirestation(firestation);
		if (station == null) {
			logger.error("Echec de la création, caserne déjà existante : {}", firestation);
		}
		return station;
	}

	public Firestation updateFirestation(Firestation firestation) {
		logger.debug("Tentative de mise à jour de la caserne : {}", firestation);
		Firestation updated = firestationRepository.updateFirestation(firestation);
		if (updated == null) {
			logger.error("Echec de la mise à jour, caserne introuvable : {}", firestation);
		}
		return updated;

	}

	public boolean deleteFirestation(String address) {
		logger.debug("Tentative de suppression de la caserne : {}", address);
		boolean deleted = firestationRepository.deleteFirestation(address);
		if(deleted) {
			logger.info("Caserne supprimée avec succès");
		}else {
			logger.error("Echec de la suppression, caserne non trouvée");
		}return deleted;
	}

	
	
	
	
	
	
	public List<Firestation> getStation_ByStationNumber(int stationNumber) throws IOException {

		List<Firestation> stations = new ArrayList<Firestation>();

		List<Firestation> firestations = getAllFirestation();

		logger.info("Lecture des firestations et recherche par stationNumber");
		for (Firestation firestation : firestations) {
			if (firestation.getStation() == stationNumber) {
				stations.add(firestation);

			}

		}
		logger.info("Nombre de firestations ayant " + stationNumber + " comme station : " + stations.size());
		return stations;
	}

}
