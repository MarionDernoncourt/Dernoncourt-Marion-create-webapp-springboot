package dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChildrendInfoDTO {

	private List<ChildInfoDTO> coveredChildren;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ChildInfoDTO {
		private String firstName;
		private String lastName;
		private int age;
		private List<ResidentInfoDTO> otherResidents;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ResidentInfoDTO {
		private String firstName;
		private String lastName;
	}

}
