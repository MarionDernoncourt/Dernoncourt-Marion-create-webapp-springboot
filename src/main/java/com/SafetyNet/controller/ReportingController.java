package com.SafetyNet.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.service.ReportingService;

import dto.ChildrendInfoDTO;
import dto.EmailInfoDTO;
import dto.FireResidentInfoDTO;
import dto.FirestationCoverageDTO;
import dto.FloodHouseholdInfoDTO;
import dto.PhoneNumberDTO;
import dto.ResidentInfoLByLastNameDTO;

/**
 * Controller REST pour du reporting sur les informations de sécurité des
 * résidents. Ce controller expose des API permettant de récupérer des données
 * liées aux résidents : couverture par les casernes de pompiers, enfants vivant
 * à une adresse, numéros de téléphone rattachés a un numéro de caserne,
 * informations en cas d'incendie, d'inondations, récupération des données par
 * un nom de famille, récupération des emails des résidents d'une ville donnée.
 * 
 * Utilise {@link Reporting Service} pour la logique métier et renvoie des
 * réponses sous forme de DTOs.
 * 
 * @see ReportingService
 * @see FirestationCoverageDTO
 * @see ChildrendInfoDTO
 * @see PhoneNumberDTO
 * @see FireResidentInfoDTO
 * @see FloodHouseholdInfoDTO
 * @see ResidentInfoLByLastNameDTO
 * @see EmailInfoDTO
 */
@RestController
public class ReportingController {

	private static Logger logger = LoggerFactory.getLogger(ReportingController.class);

	@Autowired
	public ReportingService reportingService;

	/**
	 * Récupère les informations concernant les résidents couverts par une caserne
	 * de pompiers donnée.
	 * 
	 * @param stationNumber:Numéro de la caserne dont on souhaite connaitre les
	 *                             résidents couverts
	 * @return Reponse HTTP avec un objet {@link FirestationCoverageDTO} contenant
	 *         les informations des résidents couvets, ou une erruer 404 NOT FOUND
	 *         si le numéro de caserne n'est pas trouvé
	 */
	@GetMapping("/firestation")
	public ResponseEntity<FirestationCoverageDTO> getResidentCoveredByFirestation(@RequestParam int stationNumber) {
		logger.debug("Requete reçue GET firestation coverage with stationNumber : {}", stationNumber);
		try {
			FirestationCoverageDTO coveredPersons = reportingService.getResidentCoveredByFirestation(stationNumber);
			if (coveredPersons == null) {
				logger.debug("Reponse reçue 404 NOT FOUND, le numéro de caserne {} n'est pas référencé", stationNumber);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			logger.info("Reponse reçue 200 OK, nombre de personnes couvertes par les stations {} : {}", stationNumber,
					coveredPersons.getCoveredResident().size());
			return ResponseEntity.status(HttpStatus.OK).body(coveredPersons);

		} catch (Exception e) {
			logger.error(
					"Réponse reçue 500 INTERNAL SERVER ERROR, une erreur est survenue lors de la récupération des habitants couverts par la station {} : {}",
					stationNumber, e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Récupère les informations concernant les enfants vivant à une adresse donnée
	 * 
	 * @param address : adresse pour laquelle on souhaite obtenir les informations
	 * @return Réponse HTTP avec un objet {@link ChildrendInfoDTO} concernant les
	 *         informations des enfants vivant à l'adresse donnée, ou une erreur 404
	 *         si aucun résident à l'adresse fournie.
	 */
	@GetMapping("/childAlert")
	public ResponseEntity<ChildrendInfoDTO> getChildrenInfoByAddress(@RequestParam String address) {
		logger.debug("Requete reçue GET childAlert, enfants vivant à l'adresse {}", address);
		try {
			ChildrendInfoDTO coveredChildren = reportingService.getChildrenInfoByAddress(address);
			if (coveredChildren == null) {
				logger.error("Réponse reçue 404 NOT FOUND, aucun habitant connu à l'adresse {}", address);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			logger.info("Reponse reçue 200 OK, nombre d'enfants vivants à cette adresse : {}",
					coveredChildren.getCoveredChildren().size());
			return ResponseEntity.status(HttpStatus.OK).body(coveredChildren);
		} catch (Exception e) {
			logger.error(
					"Réponse reçue 500 INTERNAL SERVER ERROR, erreur lors de la récupération des enfants vivant à l'adresse {} : {}",
					address, e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	/**
	 * Récupère les numéros de téléphone des habitants rattachés à un numéro de
	 * caserne donné.
	 * 
	 * @param firestation : numéro de station dont on souhaite connaitre les numéros
	 *                    de téléphone rattachés
	 * @return une réponse HTTP avec un objet {@link PhoneNumberDTO} contenant une
	 *         liste de numéro de téléphone, ou une erreur 404 si le numéro de
	 *         station n'est pas trouvé
	 */
	@GetMapping("/phoneAlert")
	public ResponseEntity<PhoneNumberDTO> getPhoneNumberByFirestation(@RequestParam int firestation) {
		logger.debug(
				"Requete reçue GET phoneAlert, récupération des numéros de téléphone des résidents couvert par les stations {}",
				firestation);
		try {
			PhoneNumberDTO phoneNumber = reportingService.getPhoneNumberByFirestation(firestation);
			if (phoneNumber == null) {
				logger.error("Réponse 404 NOT FOUND, le numéro de station {} n'est pas connu", firestation);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			logger.info("Réponse reçue 200 OK, nombre de numéros de téléphone récupérés : {}",
					phoneNumber.getPhoneNumber().size());
			return ResponseEntity.status(HttpStatus.OK).body(phoneNumber);
		} catch (Exception e) {
			logger.error(
					"Réponse reçue 500 INTERNAL SERVER ERROR, erreur survenue lors de la récupération des numéros de téléphone",
					e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	/**
	 * Récupère les informationssur les résidents vivant à une adresse en cas
	 * d'incendie.
	 * 
	 * @param address : L'adresse pour laquelle on souhaite obtenir les informations
	 *                sur les résidents
	 * @return une réponse HTTP avec un objet {@link FireResidentInfoDTO} contenant
	 *         les informations sur les résidents vivant à l'adresse donnée, ou une
	 *         erreur 404 si aucun résident trouvé à l'adresse fournie
	 */
	@GetMapping("/fire")
	public ResponseEntity<FireResidentInfoDTO> getFireInfoByAddress(@RequestParam String address) {
		logger.debug("Requete reçue GET fire informations concernant les habitants vivant à l'adresse {}", address);
		try {
			FireResidentInfoDTO residents = reportingService.getFireInfoByAddress(address);
			if (residents == null) {
				logger.error("Réponse reçue 404 NOT FOUND, aucun résident trouvé à l'adresse {}", address);

				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			logger.info("Réponse reçue 200 OK, nombre de résident trouvé : {}", residents.getResidents().size());
			return ResponseEntity.status(HttpStatus.OK).body(residents);
		} catch (Exception e) {
			logger.error(
					"Reponse reçue 500 INTERNAL SERVER ERROR, erreur lors de la récupération des informations vivant à l'adresse {} : {}",
					address, e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	/**
	 * Récupère les informations des résidents couverts par les numéros de station
	 * donnés en paramètre en cas d'inondation
	 * 
	 * @param stations : liste de numéro de station pour lesquelles on souhaite
	 *                 obtenir les informations sur les résidents en cas
	 *                 d'inondation
	 * @return réponse HTTP avec un objet {@link FloodHouseholdInfoDTO} contenant
	 *         une liste de foyers desservis par les stations, ou une erreur 404 NOT
	 *         FOUND si les numéros de station fournis ne sont pas connus
	 */
	@GetMapping("/flood/stations")
	public ResponseEntity<List<FloodHouseholdInfoDTO>> getFloodInfobyStation(@RequestParam List<Integer> stations) {
		logger.debug("Requete GET flood information sur les foyers desservis par les casernes {}", stations);
		try {
			List<FloodHouseholdInfoDTO> residents = reportingService.getFloodInfobyStation(stations);
			if (residents == null) {
				logger.error("Reponse reçue 404 NOT FOUND, les numéros de stations fournis {} ne sont pas connus",
						stations);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			logger.info("Reponse reçue 200 OK, nombre de foyers desservis par les casernes {} : {}", stations,
					residents.size());
			return ResponseEntity.status(HttpStatus.OK).body(residents);
		} catch (Exception e) {
			logger.error(
					"Reponse reçue 500 INTERNAL SERVER ERROR, une erreur est survenue lors de la récupération des foyers desservis par les stations {} : {}",
					stations, e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	/**
	 * Récupèreles inforamtions sur chaque habitant selon son nom de famille
	 * 
	 * @param lastName : nom de famille des résidents à chercher
	 * @return réponse HTTP avec un objet {@link ResidentInfoLByLastNameDTO}
	 *         contenant les informations des résidents trouvés, ou une erreur 404
	 *         si aucun résident n'est trouvé avec ce nom
	 */
	@GetMapping("/personInfolastName={lastName}")
	public ResponseEntity<ResidentInfoLByLastNameDTO> getResidentInfoByLastName(@PathVariable String lastName) {
		logger.debug("Requete reçue GET personInfolastName={}", lastName);
		try {
			ResidentInfoLByLastNameDTO residents = reportingService.getResidentInfoByLastName(lastName);
			if (residents == null) {
				logger.error("Reponse reçue 404 NOT FOUND, aucun résident trouvé avec le nom {}", lastName);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			logger.info("Reponse reçue 200 OK, nombre de personnes portant le nom {} : {}", lastName,
					residents.getResidents().size());
			return ResponseEntity.status(HttpStatus.OK).body(residents);
		} catch (Exception e) {
			logger.error(
					"Reponse reçue 500 INTERNAL SERVER ERROR, une erreur est survenue lors de la récupération des informations concernant les habitants portant le nom {} : {}",
					lastName, e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Récupère tous les emails des résidents d'une ville donnée
	 * 
	 * @param city : la ville dont on souhaite chercher les emails de ses habitants
	 * @return une réponse HTTP avec un objet {@link EmailInfoDTO} contenant une
	 *         liste d'email ou une erreur 404 si aucun résident n'est trouvé dans
	 *         la ville donnée.
	 */
	@GetMapping("/communityEmail")
	public ResponseEntity<EmailInfoDTO> getEmailByCity(@RequestParam String city) {
		logger.debug("Requete reçue GET communityEmail pour la ville {}", city);
		try {
			EmailInfoDTO emails = reportingService.getEmailByCity(city);
			if (emails == null) {
				logger.error("Réponse reçue 404 NOT FOUND, aucun résident trouvé dans la ville {}", city);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			logger.info("Réponse reçue 200 OK, nombre d'émail collecté pour les résidents de la ville de {} : {}", city,
					emails.getEmails().size());
			return ResponseEntity.status(HttpStatus.OK).body(emails);
		} catch (Exception e) {
			logger.error(
					"Reponse reçue 500 INTERNAL SERVER ERROR, une erreur est survenue lors de la récupération des emails des habitants de {}",
					city, e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

}
