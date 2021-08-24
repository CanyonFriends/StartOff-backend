package kr.startoff.backend.payload.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import kr.startoff.backend.entity.SkillTag;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SkillTagResponse {
	private Long skillId;
	private String skillName;
	private String color;
	private String textColor;

	public SkillTagResponse(SkillTag skillTag) {
		this.skillId = skillTag.getId();
		this.skillName = skillTag.getSkillName();
		this.color = skillTag.getColor();
		this.textColor = skillTag.getTextColor();
	}
}
