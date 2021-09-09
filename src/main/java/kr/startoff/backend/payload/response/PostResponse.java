package kr.startoff.backend.payload.response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import kr.startoff.backend.entity.Category;
import kr.startoff.backend.entity.Post;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostResponse {
	Long postId;
	String nickname;
	String title;
	String content;
	Category category;
	Integer maxPeople;
	Integer currentPeople;
	List<SkillTagResponse> postSkills;
	String createdAt;
	List<CommentResponse> comments = new ArrayList<>();

	public PostResponse(Post post) {
		this.postId = post.getId();
		this.nickname = post.getAuthor().getNickname();
		this.title = post.getTitle();
		this.content = post.getContent();
		this.category = post.getCategory();
		this.maxPeople = post.getMaxPeople();
		this.currentPeople = post.getCurrentPeople();
		this.postSkills = post.getPostWantedSkills().stream().map(SkillTagResponse::new).collect(Collectors.toList());
		this.createdAt = post.getCreatedAt().toString();
	}

	public void setComments(List<CommentResponse> comments) {
		this.comments = comments;
	}
}
