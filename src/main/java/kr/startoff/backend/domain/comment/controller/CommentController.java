package kr.startoff.backend.domain.comment.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import kr.startoff.backend.domain.comment.dto.CommentRequest;
import kr.startoff.backend.domain.comment.dto.CommentResponse;
import kr.startoff.backend.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController {
	private final CommentService commentService;

	@PostMapping("/posts/{post_id}/comments")
	public ResponseEntity<CommentResponse> saveComment(@PathVariable(value = "post_id") Long postId,
		@RequestBody CommentRequest commentRequest) {
		CommentResponse commentResponse = commentService.saveComment(postId, commentRequest);

		Map<String, Long> uriComponents = new HashMap<>();
		uriComponents.put("post_id", postId);
		uriComponents.put("comment_id", commentResponse.getCommentId());

		URI location = ServletUriComponentsBuilder
			.fromCurrentContextPath().path("/posts/{post_id}/comments/{comment_id}")
			.buildAndExpand(uriComponents).toUri();

		return ResponseEntity.created(location).body(commentResponse);
	}

	@PutMapping("/posts/{post_id}/comments/{comment_id}")
	public ResponseEntity<CommentResponse> updateComment(@PathVariable(value = "post_id") Long postId,
		@PathVariable(value = "comment_id") Long commentId, @RequestBody CommentRequest commentRequest) {
		CommentResponse commentResponse = commentService.updateComment(commentId, commentRequest);
		return ResponseEntity.ok(commentResponse);
	}

	@DeleteMapping("/posts/{post_id}/comments/{comment_id}")
	public ResponseEntity<Long> deleteComment(@PathVariable(value = "post_id") Long postId,
		@PathVariable(value = "comment_id") Long commentId) {
		return ResponseEntity.ok(commentService.deleteComment(commentId));
	}

}
