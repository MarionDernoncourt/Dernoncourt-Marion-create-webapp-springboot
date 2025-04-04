package com.SafetyNet.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.SafetyNet.model.Firestation;

/**
 * Interface définissant les différentes opérations de gestion des données
 * relatives aux casernes de pompiers (récupération, mise à jour, création et
 * suppression)
 */
@Repository
public interface IFirestationRepository {

	/**
	 * Récupère la liste de toutes les casernes de pompiers
	 * 
	 * @return une liste de {@link Firestation }
	 */
	public List<Firestation> getAllFirestation();

	/**
	 * Création d'une nouvelle caserne de pompiers
	 * 
	 * @param firestation : nouvelle caserne à créer
	 * @return Firestation : la caserne créée
	 */
	public Firestation createFirestation(Firestation firestation);

	/**
	 * Mise à jour d'une caserne de pompiers
	 * 
	 * @param firestation : nouvelles informations concernant la caserne de pompiers
	 * @return Firestation : la caserne mise à jour
	 */
	public Firestation updateFirestation(Firestation firestation);

	/**
	 * Suppression d'une caserne de pompier ou d'un mapping
	 * 
	 * @param address       : adresse de la caserne de pompiers
	 * @param stationNumber : numéro de station
	 * @return boolean : True si la caserne a été supprimée, sinon False.
	 */
	public boolean deleteFirestation(String address, Integer stationNumber);

}
