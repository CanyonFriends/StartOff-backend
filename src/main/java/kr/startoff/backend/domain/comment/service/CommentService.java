package kr.startoff.backend.domain.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.startoff.backend.domain.comment.domain.Comment;
import kr.startoff.backend.domain.comment.domain.DeleteStatus;
import kr.startoff.backend.domain.post.domain.Post;
import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.common.exception.custom.CommentNotFoundException;
import kr.startoff.backend.common.exception.custom.PostNotFoundException;
import kr.startoff.backend.common.exception.custom.UserNotFoundException;
import kr.startoff.backend.domain.comment.dto.CommentRequest;
import kr.startoff.backend.domain.comment.dto.CommentResponse;
import kr.startoff.backend.domain.comment.repository.CommentRepository;
import kr.startoff.backend.domain.post.repository.PostRepository;
import kr.startoff.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final PostRepository postRepository;

	@Transactional
	public CommentResponse saveComment(Long postId, CommentRequest commentRequest) {
		User writer = userRepository.findById(commentRequest.getUserId()).orElseThrow(UserNotFoundException::new);
		Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
		Long parentId = commentRequest.getParentId();
		if (hasParentComment(parentId)) {
			Comment parent = commentRepository.findById(parentId).orElseThrow(CommentNotFoundException::new);
			return new CommentResponse(
				commentRepository.save(Comment.createComment(writer, post, parent, commentRequest)));
		} else {
			return new CommentResponse(commentRepository.save(Comment.createComment(writer, post, commentRequest)));
		}
	}

	@Transactional
	public CommentResponse updateComment(Long commentId, CommentRequest commentRequest) {
		Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
		comment.update(commentRequest);
		return new CommentResponse(comment);
	}

	@Transactional
	public Long deleteComment(Long commentId) {
		Comment comment = commentRepository.findWithParentById(commentId).orElseThrow(CommentNotFoundException::new);
		if (comment.getChildComments().isEmpty()) {
			commentRepository.delete(getDeletableAncestorComment(comment));
		} else {
			comment.setIsDeleted(DeleteStatus.Y);
		}
		return commentId;
	}

	private Comment getDeletableAncestorComment(Comment comment) {
		Comment parent = comment.getParent();
		if (parent != null && parent.getChildComments().size() == 1 && parent.getIsDeleted() == DeleteStatus.Y) {
			return getDeletableAncestorComment(parent);
		}
		return comment;
	}

	private boolean hasParentComment(Long parentId) {
		return parentId != null;
	}
}
