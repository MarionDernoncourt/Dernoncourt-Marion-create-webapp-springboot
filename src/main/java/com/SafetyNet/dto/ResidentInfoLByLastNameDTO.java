package com.SafetyNet.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResidentInfoLByLastNameDTO {

	private List<Resident> residents;
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Resident {
		private String lastName;
		private String address;
		private int age;
		private String email;
		private List<MedicationRecordByLastName> medicationRecord;
	}
	
	@Data 
	@AllArgsConstructor
	@NoArgsConstructor
	public static class MedicationRecordByLastName {
		private List<String> medication;
		private List<String> allergies;
	}
}
