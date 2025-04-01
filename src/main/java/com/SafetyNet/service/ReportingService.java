package com.SafetyNet.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SafetyNet.model.MedicalRecord;
import com.SafetyNet.model.Person;

import dto.ChildrendInfoDTO;
import dto.ChildrendInfoDTO.ChildInfoDTO;
import dto.ChildrendInfoDTO.ResidentInfoDTO;
import dto.EmailInfoDTO;
import dto.FireResidentInfoDTO;
import dto.FireResidentInfoDTO.MedicationRecordFireInfo;
import dto.FireResidentInfoDTO.ResidentFireInfoDTO;
import dto.FirestationCoverageDTO;
import dto.FloodHouseholdInfoDTO;
import dto.FloodHouseholdInfoDTO.MedicationRecordFloodInfo;
import dto.FloodHouseholdInfoDTO.ResidentFloodInfoDTO;
import dto.PhoneNumberDTO;
import dto.ResidentInfoLByLastNameDTO;
import dto.ResidentInfoLByLastNameDTO.MedicationRecordByLastName;
import dto.ResidentInfoLByLastNameDTO.Resident;

@Service
public class ReportingService {
	private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

	@Autowired
	private FirestationService firestationService;

	@Autowired
	private PersonService personService;

	@Autowired
	private AgeCalculatorService ageCalculatorService;

	@Autowired
	private MedicalRecordService medicalRecordService;

	public FirestationCoverageDTO getResidentCoveredByFirestation(int stationNumber) {
		logger.debug("Tentative de récupération des personnes couvertes par la caserne numéro : {}", stationNumber);

		List<String> stationAddress = firestationService.findByStationNumber(stationNumber).stream()
				.map(station -> station.getAddress()).toList();

		if (stationAddress.isEmpty()) {
			logger.error("Aucune caserne n'est rattachée au numéro de station {}", stationNumber);
			return null;
		}

		List<Person> persons = personService.getAllPersons().stream()
				.filter(person -> stationAddress.contains(person.getAddress())).toList();

		logger.info("Nombre de personnes rattachées à la station {} : {}", stationNumber, persons.size());

		List<FirestationCoverageDTO.ResidentInfoDTO> coveredResidentDTO = persons.stream()
				.map(person -> new FirestationCoverageDTO.ResidentInfoDTO(person.getFirstName(), person.getLastName(),
						person.getAddress(), person.getPhone()))
				.toList();

		int nbOfAdults = 0;
		int nbOfChildren = 0;

		for (Person person : persons) {
			int age = ageCalculatorService.calculatePersonAge(person.getFirstName(), person.getLastName());
			if (age > 18) {
				nbOfAdults++;
			} else {
				nbOfChildren++;
			}

		}
		logger.info("Nombre d'adultes : {}, et nombre d'enfants : {]", nbOfAdults, nbOfChildren);
		return new FirestationCoverageDTO(coveredResidentDTO, nbOfAdults, nbOfChildren);
	}

	public ChildrendInfoDTO getChildrenInfoByAddress(String address) {
		logger.debug("Tentative de récupération des enfants habitant à l'addresse {}", address);

		List<Person> residents = personService.getAllPersons().stream()
				.filter(resident -> resident.getAddress().equalsIgnoreCase(address)).toList();

		logger.info("Nombre de personne vivant à cette adresse : {}", residents.size());

		if (residents.isEmpty()) {
			logger.error("Aucun résident connu a cette adresse");
			return null;
		}

		List<ChildInfoDTO> coveredChildren = new ArrayList<ChildInfoDTO>();

		logger.debug("Récupération de l'age de chaque résident vivant à l'adresse {}", address);
		for (Person resident : residents) {

			int age = ageCalculatorService.calculatePersonAge(resident.getFirstName(), resident.getLastName());

			if (age < 18) {

				List<ResidentInfoDTO> otherResidents = new ArrayList<>();

				for (Person person : residents) {
					if (!person.getFirstName().equalsIgnoreCase(resident.getFirstName())
							&& person.getLastName().equalsIgnoreCase(resident.getLastName())) {
						otherResidents.add(new ResidentInfoDTO(person.getFirstName(), person.getLastName()));
					}
				}
				coveredChildren
						.add(new ChildInfoDTO(resident.getFirstName(), resident.getLastName(), age, otherResidents));
			}
		}
		logger.info("Nombre d'enfants vivants a cette adresse : {}", coveredChildren.size());
		return new ChildrendInfoDTO(coveredChildren);
	}

	public PhoneNumberDTO getPhoneNumberByFirestation(int firestation) {
		List<String> stationAddress = firestationService.findByStationNumber(firestation).stream()
				.map(station -> station.getAddress()).toList();

		if (stationAddress.isEmpty()) {
			logger.error("Aucune caserne n'est rattachée au numéro de station {}", firestation);
			return null;
		}

		List<Person> persons = personService.getAllPersons().stream()
				.filter(person -> stationAddress.contains(person.getAddress())).toList();

		logger.info("Nombre de personnes rattachées à la station {} : {}", firestation, persons.size());

		Set<String> phoneNumber = new HashSet<String>();

		for (Person person : persons) {
			phoneNumber.add(person.getPhone());
		}
		return new PhoneNumberDTO(phoneNumber);
	}

	public FireResidentInfoDTO getFireInfoByAddress(String address) {

		logger.debug("Tentative de récupération des habitants vivant à l'adresse {}", address);

		List<Person> residents = personService.getAllPersons().stream()
				.filter(resident -> resident.getAddress().equalsIgnoreCase(address)).toList();

		if (residents.isEmpty()) {
			logger.error("Aucun habitant à l'adresse {}", address);
			return null;
		}

		List<ResidentFireInfoDTO> residentFireInfo = new ArrayList<>();

		for (Person resident : residents) {

			List<MedicationRecordFireInfo> medicationsRecord = new ArrayList<>();

			int age = ageCalculatorService.calculatePersonAge(resident.getFirstName(), resident.getLastName());

			MedicalRecord medRecord = medicalRecordService.getMedicalRecord(resident.getFirstName(),
					resident.getLastName());

			medicationsRecord.add(new MedicationRecordFireInfo(medRecord.getMedications(), medRecord.getAllergies()));

			ResidentFireInfoDTO residentInfo = new ResidentFireInfoDTO(resident.getLastName(), resident.getPhone(), age,
					medicationsRecord);

			residentFireInfo.add(residentInfo);
		}

		List<Integer> stationNumber = firestationService.getAllFirestation().stream()
				.filter(station -> station.getAddress().equalsIgnoreCase(address)).map(station -> station.getStation())
				.collect(Collectors.toList());

		logger.info("Nombre d'habitants à l'adresse {} : {}, couverts par la(les) caserne(s) {}", address,
				residentFireInfo.size(), stationNumber);
		return new FireResidentInfoDTO(residentFireInfo, stationNumber);
	}

	public List<FloodHouseholdInfoDTO> getFloodInfobyStation(List<Integer> stations) {

		logger.debug("Tentative de récupération des foyers desservis par la liste de casernes :{}", stations);
		List<FloodHouseholdInfoDTO> households = new ArrayList<>();

		for (Integer station : stations) {

			List<String> stationsAddress = firestationService.findByStationNumber(station).stream()
					.map(firestation -> firestation.getAddress()).collect(Collectors.toList());

			if (stationsAddress.isEmpty()) {
				logger.error("Le numéro de caserne {} n'est pas connu", station);

			}
			for (String address : stationsAddress) {
				logger.debug("Tentative de récupération des résident habitant à l'addresse {}", address);

				List<Person> residents = personService.getAllPersons().stream()
						.filter(person -> person.getAddress().equalsIgnoreCase(address)).toList();

				List<ResidentFloodInfoDTO> residentInfo = new ArrayList<>();

				for (Person resident : residents) {
					logger.debug("Tentative de récupération des informations concernant le résident {}", resident);

					int age = ageCalculatorService.calculatePersonAge(resident.getFirstName(), resident.getLastName());

					List<MedicationRecordFloodInfo> medicationsRecord = new ArrayList<>();
					MedicalRecord medRecord = medicalRecordService.getMedicalRecord(resident.getFirstName(),
							resident.getLastName());

					medicationsRecord
							.add(new MedicationRecordFloodInfo(medRecord.getMedications(), medRecord.getAllergies()));

					residentInfo.add(new ResidentFloodInfoDTO(resident.getLastName(), resident.getPhone(), age,
							medicationsRecord));
				}

				logger.info("Nombre de résidents habitant à l'adresse {} : {}", address, residents.size());
				households.add(new FloodHouseholdInfoDTO(address, residentInfo));

			}
		}
		logger.info("Nombre de foyer desservis par les différentes casernes : {}", households.size());
		return households;
	}

	public ResidentInfoLByLastNameDTO getResidentInfoByLastName(String lastName) {
logger.info("Tentative de récupération des informations concernant les résidents portant le nom {}", lastName);
		List<Resident> residents = new ArrayList<>();

		List<Person> persons = personService.getAllPersons().stream()
				.filter(person -> person.getLastName().equalsIgnoreCase(lastName)).toList();
	
		if(persons.isEmpty()) {
			logger.error("Aucun résident trouvé pour le nom {}", lastName);
			return null;
		}
		for(Person person : persons) {
		
			int age = ageCalculatorService.calculatePersonAge(person.getFirstName(), person.getLastName());
			
			List<MedicationRecordByLastName> medicationsRecord = new ArrayList<>();
		
			MedicalRecord medRecord = medicalRecordService.getMedicalRecord(person.getFirstName(),
					person.getLastName());

			medicationsRecord
					.add(new MedicationRecordByLastName(medRecord.getMedications(), medRecord.getAllergies()));

			residents.add(new Resident(person.getLastName(), person.getAddress(), age, person.getEmail(), medicationsRecord));
		}
		logger.info("Nombre de résidents avec le nom {} : {}", lastName, residents.size());
		return new ResidentInfoLByLastNameDTO(residents);
	}
	
	public EmailInfoDTO getEmailByCity(String city) {
		logger.debug("Tentative de récupération des mails des habitants de {]", city);
		List<String> emails = new ArrayList<String>()	;
		
		List<Person> residents = personService.getAllPersons().stream().filter(person -> person.getCity().equalsIgnoreCase(city)).toList()	;
		
		if(residents.isEmpty()) {
			logger.error("Aucun habitant trouvé dans la ville {}", city);
			return null;
		}
		for(Person resident : residents) {
			emails.add(resident.getEmail());
		}
		logger.info("Nombre de mails récupérés : {}", emails.size());
		return new EmailInfoDTO(emails);
	}

}
