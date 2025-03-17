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
		return dataLoaderRepository.getAllFirestation();

	}

	@Override
	public Firestation getFirestationByAddress(String address) {

		List<Firestation> firestations = dataLoaderRepository.getAllFirestation();
		return firestations.stream().filter(station -> station.getAddress().equalsIgnoreCase(address)).findFirst()
				.orElse(null);
	}

	@Override
	public Firestation createFirestation(Firestation firestation) {

		List<Firestation> firestations = dataLoaderRepository.getAllFirestation();

		for (Firestation station : firestations) {
			if (station.getAddress().equalsIgnoreCase(firestation.getAddress())) {
				logger.warn("Cette station existe déjà");
				return null;
			}
		}
		firestations.add(firestation);
		return firestation ;
	}

	@Override
	public Firestation updateFirestation(Firestation firestation) {
		List<Firestation> firestations = dataLoaderRepository.getAllFirestation();

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
	public boolean deleteFirestation(String address ) {
		
		List<Firestation> firestations = dataLoaderRepository.getAllFirestation()	;
		
		boolean firestationRemoved = firestations.removeIf(station -> address.equalsIgnoreCase(station.getAddress()) );
		return firestationRemoved;
	}

}