package kr.startoff.backend.domain.project.dto;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProjectRequest {
	private String title;
	private String introduce;
	private String content;
	private String githubUrl;
	private String deployUrl;
	private String startDate;
	private String endDate;
	private List<String> projectSkills;

	public ProjectRequest(String title, String introduce, String content, String githubUrl, String deployUrl,
		String startDate, String endDate, List<String> projectSkills) {
		this.title = title;
		this.introduce = introduce;
		this.content = content;
		this.githubUrl = githubUrl;
		this.deployUrl = deployUrl;
		this.startDate = startDate;
		this.endDate = endDate;
		this.projectSkills = projectSkills;
	}
}
