package dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class FirestationCoverageDTO {

	private List<CoveredPersonsInformation> coveredResident;
	private int numberOfAdults;
	private int numberOfChilds;
	
	@Data
    @AllArgsConstructor
	public static class CoveredPersonsInformation {
		private String firstName;
		private String lastName;
		private String address;
		private String phone;
	}
}
