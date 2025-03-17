package com.SafetyNet.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.SafetyNet.model.Firestation;

@Repository
public class FirestationRepository implements IFirestationRepository {

	private static final Logger logger = LoggerFactory.getLogger(FirestationRepository.class);

	private IDataLoaderRepository dataLoaderRepository;

	public FirestationRepository(IDataLoaderRepository dataLoaderRepository) {
		this.dataLoaderRepository = dataLoaderRepository;
	}

	@Override
	public List<Firestation> getAllFirestation() {
		logger.debug("Accès aux données, récupération de toutes les casernes");
		List<Firestation> stations = dataLoaderRepository.getAllFirestation();
		if (stations.isEmpty()) {
			logger.error("Aucune caserne trouvée");
		}
		return stations;

	}

	@Override
	public Firestation getFirestationByAddress(String address) {
		List<Firestation> firestations = dataLoaderRepository.getAllFirestation();
		logger.debug("Accès aux données, récupération de la caserne : {}", address);
		return firestations.stream().filter(station -> station.getAddress().equalsIgnoreCase(address)).findFirst()
				.orElse(null);
	}

	@Override
	public Firestation createFirestation(Firestation firestation) {

		List<Firestation> firestations = dataLoaderRepository.getAllFirestation();
		logger.debug("Accès aux données, création de la caserne : {}", firestation);
		for (Firestation station : firestations) {
			if (station.getAddress().equalsIgnoreCase(firestation.getAddress())) {
				logger.error("Cette caserne existe déjà");
				return null;
			}
		}
		firestations.add(firestation);
		logger.info("Caserne créée avec succès : {}", firestation);
		return firestation;
	}

	@Override
	public Firestation updateFirestation(Firestation firestation) {
		List<Firestation> firestations = dataLoaderRepository.getAllFirestation();
		logger.debug("Accès aux données, mise à jour de la casern {}", firestation);
		for (Firestation station : firestations) {
			if (station.getAddress().equalsIgnoreCase(firestation.getAddress())) {
				station.setStation(firestation.getStation());
				logger.info("Caserne mise à jour avec succès : {}", firestation);
				return station;
			}
		}
		logger.error("Aucune caserne trouvée pour la mise à jour");
		return null;
	}

	@Override
	public boolean deleteFirestation(String address) {

		List<Firestation> firestations = dataLoaderRepository.getAllFirestation();
		logger.debug("Accès aux données, suppression de la caserne : {}", address);
		boolean firestationRemoved = firestations.removeIf(station -> address.equalsIgnoreCase(station.getAddress()));
		if (firestationRemoved) {
			logger.info("La caserne a été supprimée avec succès");
		} else {
			logger.error("Impossible de supprimer, caserne non trouvée");
		}
		return firestationRemoved;
	}

}