package com.SafetyNet.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FireResidentInfoDTO {

	private List<ResidentFireInfoDTO> residents;
	private List<Integer> stationNumber;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ResidentFireInfoDTO {
		private String lastName;
		private String phone;
		private int age;
		private List<MedicationRecordFireInfo> medicationRecord;
	}
	
	@Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MedicationRecordFireInfo {
		private List<String> medications;
		private List<String> allergies; 
		}
}
