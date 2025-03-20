package com.SafetyNet.repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

		return stations;

	}

	@Override
	public Firestation createFirestation(Firestation firestation) {
		logger.debug("Accès aux données, création de la caserne : {}", firestation);

		dataLoaderRepository.getAllFirestation().add(firestation);

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
	public boolean deleteFirestation(String address, Integer stationNumber) {
		logger.debug("Accès aux données, suppression de(s) la caserne(s) : {} {}", address, stationNumber);

		List<Firestation> firestations = dataLoaderRepository.getAllFirestation();
		
		if (address == null && stationNumber == null) {
			logger.error("Aucun paramètre fourni pour la suppression");
			return false;
		}
		boolean firestationRemoved = firestations
				.removeIf(station -> 
				(address == null || station.getAddress().equalsIgnoreCase(address)) &&
				(stationNumber == null || Objects.equals(station.getStation(), stationNumber))); // Objects.equals pour comparer int et Integer

		if (firestationRemoved) {
			logger.info("La caserne a été supprimée avec succès");
		} else {
			logger.error("Impossible de supprimer, caserne non trouvée");
		}
		return firestationRemoved;
	}

	

}