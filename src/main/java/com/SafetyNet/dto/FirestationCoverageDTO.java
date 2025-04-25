package com.SafetyNet.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FirestationCoverageDTO {

	@Data
    @AllArgsConstructor
    @NoArgsConstructor
	public static class ResidentInfoDTO {
		private String firstName;
		private String lastName;
		private String address;
		private String phone;
	}
	
	private List<ResidentInfoDTO> coveredResident;
	private int numberOfAdult;
	private int numberOfChild;
	
	
}
