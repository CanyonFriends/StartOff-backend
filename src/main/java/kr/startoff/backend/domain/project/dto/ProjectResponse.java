package kr.startoff.backend.domain.project.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import kr.startoff.backend.domain.project.domain.Project;
import kr.startoff.backend.domain.tag.dto.SkillTagResponse;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProjectResponse {
	private Long id;
	private String title;
	private String introduce;
	private String content;
	private String githubUrl;
	private String deployUrl;
	private String startDate;
	private String endDate;
	private List<SkillTagResponse> ProjectSkills;

	public ProjectResponse(Project project) {
		this.id = project.getId();
		this.title = project.getTitle();
		this.introduce = project.getIntroduce();
		this.content = project.getContent();
		this.githubUrl = project.getGithubUrl();
		this.deployUrl = project.getDeployUrl();
		this.startDate = project.getStartDate();
		this.endDate = project.getEndDate();
		this.ProjectSkills = project.getProjectSkills()
			.stream()
			.map(SkillTagResponse::new)
			.collect(Collectors.toList());
	}
}
