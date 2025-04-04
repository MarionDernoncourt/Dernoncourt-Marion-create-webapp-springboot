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

/**
 * Ce service gère la logique métier pour les différents reporting sur les
 * résidents : couverture par les casernes de pompiers, enfants vivant à une
 * adresse, numéros de téléphone rattachés a un numéro de caserne, informations
 * en cas d'incendie, d'inondations, récupération des données par un nom de
 * famille, récupération des emails des résidents d'une ville donnée.
 * 
 * Utilise {@link FirestationService}, {@link PersonService},
 * {@link AgeCalculatorService}, {@link MedicalRecordService} pour récupérer les
 * données nécessaires
 * 
 * @see FirestationService
 * @see PersonService
 * @see AgeCalculatorService
 * @see MedicalRecordService
 * @see FirestationCoverageDTO
 * @see ChildrendInfoDTO
 * @see PhoneNumberDTO
 * @see FireResidentInfoDTO
 * @see FloodHouseholdInfoDTO
 * @see ResidentInfoLByLastNameDTO
 * @see EmailInfoDTO
 */
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

	/**
	 * Récupère les résidents couvets par un numéro de caserne donné
	 * 
	 * @param stationNumber : numéro de station
	 * @return un objet {@link FirestationCoverageDTO} contenant une liste de
	 *         résidents ainsi que le nombre d'adultes et d'enfants, ou null si le
	 *         numéro de station n'est pas connu
	 */
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

	/**
	 * Récupère les enfants vivant à l'adresse fournie en paramètre
	 * 
	 * @param address : adresse dont souhaite connaitre les enfants y vivant
	 * @return un objet {@link ChildrendInfoDTO} contenant une liste d'enfant vivant
	 *         a cette adresse, ou null si aucun résident n'habite à cette adresse
	 */
	public ChildrendInfoDTO getChildrenInfoByAddress(String address) {
		logger.debug("Tentative de récupération des enfants habitant à l'addresse {}", address);

		List<Person> residents = personService.getAllPersons().stream()
				.filter(resident -> resident.getAddress().equalsIgnoreCase(address)).toList();

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

	/**
	 * Récupère les numéros de téléphone des résidents desservis par le numéro de
	 * caserne fourni
	 * 
	 * @param firestation: numéro de station
	 * @return un objet {@link PhoneNumberDTO} contenant une liste de numéro de
	 *         téléphone, ou null si le numéro de station n'est pas connu
	 */
	public PhoneNumberDTO getPhoneNumberByFirestation(int firestation) {
		logger.debug("Tentative de récupération des numéro de téléphone rattachés aux station {}", firestation);

		List<String> stationAddress = firestationService.findByStationNumber(firestation).stream()
				.map(station -> station.getAddress()).toList();

		if (stationAddress.isEmpty()) {
			logger.error("Aucune caserne n'est rattachée au numéro de station {}", firestation);
			return null;
		}

		List<Person> persons = personService.getAllPersons().stream()
				.filter(person -> stationAddress.contains(person.getAddress())).toList();

		Set<String> phoneNumber = new HashSet<String>();

		for (Person person : persons) {
			phoneNumber.add(person.getPhone());
		}
		return new PhoneNumberDTO(phoneNumber);
	}

	/**
	 * Récupère les informations sur les résidents d'une adresse donnée
	 * 
	 * @param address : adresse
	 * @return un objet {@link FireResidentInfoDTO} contenant une liste des
	 *         résidents vivant à l'adresse donnée ainsi que les numéros de caserne
	 *         desservant l'adresse donnée ou null si aucun habitant n'est trouvé à
	 *         cette adresse
	 */
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

	/**
	 * Récupère les informations des habitants en cas d'inondation selon une liste
	 * de numéro de station donnée
	 * 
	 * @param stations : liste des stations
	 * @return un objet {@link FloodHouseholdInfoDTO} contenant une liste des foyers
	 *         desservis par les stations données en paramètre
	 */
	public List<FloodHouseholdInfoDTO> getFloodInfobyStation(List<Integer> stations) {

		logger.debug("Tentative de récupération des foyers desservis par la liste de casernes :{}", stations);
		List<FloodHouseholdInfoDTO> households = new ArrayList<>();

		for (Integer station : stations) {

			List<String> stationsAddress = firestationService.findByStationNumber(station).stream()
					.map(firestation -> firestation.getAddress()).collect(Collectors.toList());

			if (stationsAddress.isEmpty()) {
				logger.error("Le numéro de caserne {} n'est pas connu", station);
				return null;
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

				households.add(new FloodHouseholdInfoDTO(address, residentInfo));

			}
		}
		logger.info("Nombre de foyer desservis par les différentes casernes : {}", households.size());
		return households;
	}

	/**
	 * Récupère les informations des résidents selon le nom de famille donnée
	 * 
	 * @param lastName : nom de famille
	 * @return un objet {@link ResidentInfoLByLastNameDTO} contenant une liste de
	 *         résident avec les informations nécessaires pour chaque habitant
	 */
	public ResidentInfoLByLastNameDTO getResidentInfoByLastName(String lastName) {
		logger.debug("Tentative de récupération des informations concernant les résidents portant le nom {}", lastName);

		List<Resident> residents = new ArrayList<>();

		List<Person> persons = personService.getAllPersons().stream()
				.filter(person -> person.getLastName().equalsIgnoreCase(lastName)).toList();

		if (persons.isEmpty()) {
			logger.error("Aucun résident trouvé pour le nom {}", lastName);
			return null;
		}
		for (Person person : persons) {
			logger.debug("Tentative de récupération des information du résident {} {}", person.getFirstName(),
					person.getLastName());

			int age = ageCalculatorService.calculatePersonAge(person.getFirstName(), person.getLastName());

			List<MedicationRecordByLastName> medicationsRecord = new ArrayList<>();

			MedicalRecord medRecord = medicalRecordService.getMedicalRecord(person.getFirstName(),
					person.getLastName());

			medicationsRecord.add(new MedicationRecordByLastName(medRecord.getMedications(), medRecord.getAllergies()));

			residents.add(
					new Resident(person.getLastName(), person.getAddress(), age, person.getEmail(), medicationsRecord));
		}

		logger.info("Nombre de résidents avec le nom {} : {}", lastName, residents.size());
		return new ResidentInfoLByLastNameDTO(residents);
	}

	
	/**
	 * Récupère les emails des habitants d'une ville donnée
	 * 
	 * @param city : ville pour laquelle on souhaite récupérer les emails	 
	 * @return un objet {@link EmailInfoDTO} contenant une liste d'emails
	 */
	public EmailInfoDTO getEmailByCity(String city) {
		logger.debug("Tentative de récupération des mails des habitants de {]", city);

		List<String> emails = new ArrayList<String>();

		List<Person> residents = personService.getAllPersons().stream()
				.filter(person -> person.getCity().equalsIgnoreCase(city)).toList();

		if (residents.isEmpty()) {
			logger.error("Aucun habitant trouvé dans la ville {}", city);
			return null;
		}
		for (Person resident : residents) {
			logger.debug("Tentative de récupération de l'email du résident {} {}", resident.getFirstName(),
					resident.getLastName());
			emails.add(resident.getEmail());
		}
		logger.info("Nombre de mails récupérés : {}", emails.size());
		return new EmailInfoDTO(emails);
	}

}
