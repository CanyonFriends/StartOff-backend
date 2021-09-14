package kr.startoff.backend.payload.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import kr.startoff.backend.entity.Comment;
import kr.startoff.backend.entity.DeleteStatus;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentResponse {
	Long commentId;
	Long userId;
	String nickname;
	String content;
	String createdAt;
	DeleteStatus isDeleted;
	List<CommentResponse> childComment = new ArrayList<>();

	public CommentResponse(Comment comment) {
		this.commentId = comment.getId();
		this.userId = comment.getWriter().getId();
		this.nickname = comment.getWriter().getNickname();
		this.content = comment.getContent();
		this.isDeleted = comment.getIsDeleted();
		this.createdAt = comment.getCreatedAt().toString();
	}

	public void addChildComment(CommentResponse child) {
		childComment.add(child);
	}

}
