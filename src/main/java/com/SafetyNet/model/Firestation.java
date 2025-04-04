package com.SafetyNet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Représente une caserne de pompiers avec son adresse et son numéro de station.
 * L'adresse est un String, et le numéro de station un int
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Firestation {

	private String address;
	private int station;

}
