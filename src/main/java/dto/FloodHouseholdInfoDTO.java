package dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FloodHouseholdInfoDTO {

	private String address;
	private List<ResidentFloodInfoDTO> residents;


	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ResidentFloodInfoDTO {
		private String lastName;
		private String phone;
		private int age;
		private List<MedicationRecordFloodInfo> medicationRecord;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class MedicationRecordFloodInfo {
		private List<String> medications;
		private List<String> allergies;
	}

}
