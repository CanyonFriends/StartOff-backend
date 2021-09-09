package kr.startoff.backend.payload.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentRequest {
	Long userId;
	Long parentId;
	String content;

	public CommentRequest(Long userId, Long parentId, String content) {
		this.userId = userId;
		this.parentId = parentId;
		this.content = content;
	}
}
