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

	private InitializationListsRepository initializationListsRepository;

	public FirestationRepository(InitializationListsRepository initializationListsRepository) {
		this.initializationListsRepository = initializationListsRepository;
	}

	@Override
	public List<Firestation> getAllFirestation() {
		return initializationListsRepository.getAllFirestation();

	}

	@Override
	public Firestation getFirestation_ByAddress(String address) {

		List<Firestation> firestations = initializationListsRepository.getAllFirestation();
		return firestations.stream().filter(station -> station.getAddress().equalsIgnoreCase(address)).findFirst()
				.orElse(null);
	}

	@Override
	public void createFirestation(Firestation firestation) {

		List<Firestation> firestations = initializationListsRepository.getAllFirestation();

		for (Firestation station : firestations) {
			if (station.getAddress().equalsIgnoreCase(firestation.getAddress())) {
				logger.warn("Cette station existe déjà");
			}
		}
		firestations.add(firestation);
	}

	@Override
	public Firestation updateFirestation(Firestation firestation) {
		List<Firestation> firestations = initializationListsRepository.getAllFirestation();

		for (Firestation station : firestations) {
			if (station.getAddress().equalsIgnoreCase(firestation.getAddress())) {
								station.setStation(firestation.getStation());
				return station;
			}
		}
		logger.warn("Aucune caserne trouvée");
		return null;
	}

	@Override
	public void deleteFirestation(Firestation firestation ) {
		
		List<Firestation> firestations = initializationListsRepository.getAllFirestation()	;
		
		firestations.removeIf(station -> firestation.getAddress().equalsIgnoreCase(station.getAddress()) && firestation.getStation() == station.getStation());
	}

}