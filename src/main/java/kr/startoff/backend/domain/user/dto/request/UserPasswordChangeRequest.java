package kr.startoff.backend.domain.user.dto.request;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserPasswordChangeRequest {
	@NotBlank
	private String beforePassword;
	@NotBlank
	private String afterPassword;

	public UserPasswordChangeRequest(String beforePassword, String afterPassword) {
		this.beforePassword = beforePassword;
		this.afterPassword = afterPassword;
	}
}
