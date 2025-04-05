package com.SafetyNet.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.SafetyNet.model.Firestation;
import com.SafetyNet.repository.IFirestationRepository;

/**
 * Service permettant de gérer les opérations liées aux casernes de pompiers.
 * Fournit des méthodes pour récupérer, créer, mettre à jour, supprimer et
 * rechercher des casernes.
 */
@Service
public class FirestationService {

	private static final Logger logger = LoggerFactory.getLogger(FirestationService.class);

	private IFirestationRepository firestationRepository;

	/**
	 * Constructeur injectant la dépendance vers le repository des casernes.
	 *
	 * @param firestationRepository le repository permettant l'accès aux données des
	 *                              casernes
	 */
	public FirestationService(IFirestationRepository firestationRepository) {
		this.firestationRepository = firestationRepository;
	}

	/**
	 * Récupère la liste de toutes les casernes.
	 *
	 * @return une liste de {@link Firestation} représentant toutes les casernes
	 */
	public List<Firestation> getAllFirestation() {
		logger.debug("Tentative de récupération de toutes les casernes");

		List<Firestation> stations = firestationRepository.getAllFirestation();

		return stations;

	}

	/**
	 * Crée une nouvelle caserne. Vérifie si la caserne existe déjà et si les
	 * informations sont valides.
	 *
	 * @param firestation l'objet {@link Firestation} à créer
	 * @return la caserne créée ou {@code null} si la création a échoué
	 * @throws ResponseStatusException si une erreur de validation survient (adresse
	 *                                 ou numéro de station invalide)
	 */
	public Firestation createFirestation(Firestation firestation) {
		logger.debug("Tentative de création de la caserne : {}", firestation);

		List<Firestation> stations = getAllFirestation();
		for (Firestation station : stations) {
			if (station == firestation) {
				logger.error("Echec de la création : caserne déjà existante : {}", firestation);
				return null;
			}
		}
		if ((firestation.getAddress() == null || firestation.getAddress() == "") || firestation.getStation() == 0) {
			logger.error("Echec de la création : merci d'entrer une adresse et un numéro de station valide");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Une caserne existe déjà a cette adresse");

		}
		Firestation station = firestationRepository.createFirestation(firestation);

		return station;
	}

	/**
	 * Met à jour une caserne existante. Si la caserne n'est pas trouvée, une
	 * exception est levée.
	 *
	 * @param firestation l'objet {@link Firestation} avec les nouvelles
	 *                    informations
	 * @return la caserne mise à jour
	 * @throws ResponseStatusException si la caserne n'a pas été trouvée
	 */
	public Firestation updateFirestation(Firestation firestation) {
		logger.debug("Tentative de mise à jour de la caserne : {}", firestation);
		Firestation updated = firestationRepository.updateFirestation(firestation);
		if (updated == null) {
			logger.error("Echec de la mise à jour, caserne introuvable : {}", firestation);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Caserne non trouvée pour la mise à jour");
		}
		return updated;

	}

	/**
	 * Supprime une caserne en fonction de l'adresse et du numéro de station.
	 *
	 * @param address       l'adresse de la caserne à supprimer
	 * @param stationNumber le numéro de station de la caserne à supprimer
	 * @return {@code true} si la suppression a été effectuée avec succès, sinon
	 *         {@code false}
	 */
	public boolean deleteFirestation(String address, Integer stationNumber) {
		logger.debug("Tentative de suppression de la caserne : {} {}", address, stationNumber);

		boolean deleted = firestationRepository.deleteFirestation(address, stationNumber);

		return deleted;
	}

	/**
	 * Recherche une caserne à partir de son adresse.
	 *
	 * @param address l'adresse de la caserne à rechercher
	 * @return la caserne correspondante ou {@code null} si non trouvée
	 */
	public Firestation findByAddress(String address) {
		logger.debug("Accès aux données, rechercher de la caserne à l'adresse : {}", address);
		Firestation stationByAddress = getAllFirestation().stream()
				.filter(station -> station.getAddress().equalsIgnoreCase(address)).findFirst().orElse(null);
		return stationByAddress;
	}

	/**
	 * Recherche toutes les casernes correspondant à un numéro de station donné.
	 *
	 * @param stationNumber le numéro de station à rechercher
	 * @return une liste de casernes correspondant au numéro de station
	 */
	public List<Firestation> findByStationNumber(int stationNumber) {
		logger.debug("Accès aux données, recherche des casernes rattachées au numéro : {}", stationNumber);
		return getAllFirestation().stream().filter(station -> station.getStation() == stationNumber)
				.collect(Collectors.toList());
	}

}
