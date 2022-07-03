package kr.startoff.backend.domain.post.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import kr.startoff.backend.domain.post.domain.Post;
import kr.startoff.backend.domain.tag.dto.SkillTagResponse;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostListResponse {
	Long postId;
	String nickname;
	String title;
	Integer maxPeople;
	Integer currentPeople;
	List<SkillTagResponse> postSkills;
	String createdAt;

	public PostListResponse(Post post) {
		this.postId = post.getId();
		this.nickname = post.getAuthor().getNickname();
		this.title = post.getTitle();
		this.maxPeople = post.getMaxPeople();
		this.currentPeople = post.getCurrentPeople();
		this.createdAt = post.getCreatedAt().toString();
		this.postSkills = post.getPostWantedSkills().stream().map(SkillTagResponse::new).collect(Collectors.toList());

	}
}
