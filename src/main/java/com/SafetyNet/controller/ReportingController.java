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

@RestController
public class ReportingController {

	private static Logger logger = LoggerFactory.getLogger(ReportingController.class);

	@Autowired
	public ReportingService reportingService;

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

	@GetMapping("/phoneAlert")
	public ResponseEntity<PhoneNumberDTO> getPhoneNumberByFirestation(@RequestParam int firestation) {
		logger.debug(
				"Requete reçue GET phoneAlert, récupération des numéros de téléphone des résidents couvert par les stations {}",
				firestation);
		try {
			PhoneNumberDTO phoneNumber = reportingService.getPhoneNumberByFirestation(firestation);
			if (phoneNumber == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.status(HttpStatus.OK).body(phoneNumber);
		} catch (Exception e) {
			logger.error(
					"Réponse reçue 500 INTERNAL SERVER ERROR, erreur survenue lors de la récupération des numéros de téléphone",
					e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	@GetMapping("/fire")
	public ResponseEntity<FireResidentInfoDTO> getFireInfoByAddress(@RequestParam String address) {
		logger.debug("Requete reçue GET fire informations concernant les habitants vivant à l'adresse {}", address);
		try {
			FireResidentInfoDTO residents = reportingService.getFireInfoByAddress(address);
			if (residents == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.status(HttpStatus.OK).body(residents);
		} catch (Exception e) {
			logger.error(
					"Reponse reçue 500 INTERNAL SERVER ERROR, erreur lors de la récupération des informations vivant à l'adresse {} : {}",
					address, e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	@GetMapping("/flood")
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
