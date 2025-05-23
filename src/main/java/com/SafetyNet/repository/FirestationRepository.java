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

	/**
	 * Constructeur injectant la dépendance vers le repository de chargement de données.
	 *
	 * @param dataLoaderRepository le repository permettant de charger les données en mémoire
	 */
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

		boolean removed = false;

		if (address == null && stationNumber != null) {

			removed = firestations.removeIf(station -> Objects.equals(station.getStation(), stationNumber));

		} else if (address != null && stationNumber == null) {

			removed = firestations.removeIf(station -> (station.getAddress().equalsIgnoreCase(address)));

		} else if (address != null && stationNumber != null) {
			removed = firestations.removeIf(station -> station.getAddress().equalsIgnoreCase(address)
					&& Objects.equals(station.getStation(), stationNumber));

		}
		if (removed) {
			logger.info("La caserne a été supprimée avec succès");
		} else {
			logger.error("Aucune caserne supprimée : adresse ou numéro de station non trouvé");
		}
		return removed;

	}

}
