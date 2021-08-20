package kr.startoff.backend.payload.request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

	public ProjectRequest(String title, String introduce, String content, String githubUrl, String deployUrl,
		String startDate, String endDate) {
		this.title = title;
		this.introduce = introduce;
		this.content = content;
		this.githubUrl = githubUrl;
		this.deployUrl = deployUrl;
		this.startDate = startDate;
		this.endDate = endDate;
	}
}
