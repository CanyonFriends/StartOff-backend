package kr.startoff.backend.domain.user.dto.response;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import kr.startoff.backend.domain.project.dto.ProjectResponse;
import kr.startoff.backend.domain.tag.dto.SkillTagResponse;
import kr.startoff.backend.domain.user.domain.Profile;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserProfileResponse {
	@NotBlank
	private String introduce;
	private String githubUrl;
	private String blogUrl;
	private String baekjoonId;
	private List<ProjectResponse> projects;
	private List<SkillTagResponse> userSkills;

	private UserProfileResponse(Profile profile) {
		this.introduce = profile.getIntroduce();
		this.githubUrl = profile.getGithubUrl();
		this.blogUrl = profile.getBlogUrl();
		this.baekjoonId = profile.getBaekjoonId();
	}

	public static UserProfileResponse from(Profile profile) {
		return new UserProfileResponse(profile);
		// TODO 수정 필요
		// this.projects = user.getProjects().stream().map(ProjectResponse::new).collect(Collectors.toList());
		// this.userSkills = user.getUserSkills().stream().map(SkillTagResponse::new).collect(Collectors.toList());
	}

}
