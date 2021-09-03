package kr.startoff.backend.payload.request;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import kr.startoff.backend.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostRequest {
	Long userId;
	String title;
	String content;
	Category category;
	List<String> postSkills;
	Integer currentPeople;
	Integer maxPeople;

	public PostRequest(Long userId, String title, String content, Category category,
		List<String> postSkills, Integer currentPeople, Integer maxPeople) {
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.category = category;
		this.postSkills = postSkills;
		this.currentPeople = currentPeople;
		this.maxPeople = maxPeople;
	}
}
