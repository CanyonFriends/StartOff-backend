package kr.startoff.backend.payload.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import kr.startoff.backend.entity.Project;
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

	public ProjectResponse(Project project) {
		this.id = project.getId();
		this.title = project.getTitle();
		this.introduce = project.getIntroduce();
		this.content = project.getContent();
		this.githubUrl = project.getGithubUrl();
		this.deployUrl = project.getDeployUrl();
		this.startDate = project.getStartDate();
		this.endDate = project.getEndDate();
	}
}
