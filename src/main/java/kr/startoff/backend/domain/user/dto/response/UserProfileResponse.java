package kr.startoff.backend.domain.user.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.domain.project.dto.ProjectResponse;
import kr.startoff.backend.domain.tag.dto.SkillTagResponse;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserProfileResponse {
	@NotBlank
	private String nickname;
	private String introduce;
	private String imageUrl;
	private String githubUrl;
	private String blogUrl;
	private String baekjoonId;
	private List<ProjectResponse> projects;
	private List<SkillTagResponse> userSkills;

	public UserProfileResponse(User user) {
		this.nickname = user.getNickname();
		this.introduce = user.getIntroduce();
		this.imageUrl = user.getImageUrl();
		this.githubUrl = user.getGithubUrl();
		this.blogUrl = user.getBlogUrl();
		this.baekjoonId = user.getBaekjoonId();
		// TODO 수정 필요
		// this.projects = user.getProjects().stream().map(ProjectResponse::new).collect(Collectors.toList());
		// this.userSkills = user.getUserSkills().stream().map(SkillTagResponse::new).collect(Collectors.toList());
	}

}
