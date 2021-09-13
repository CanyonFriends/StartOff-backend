package kr.startoff.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.startoff.backend.entity.Comment;
import kr.startoff.backend.entity.DeleteStatus;
import kr.startoff.backend.entity.Post;
import kr.startoff.backend.entity.User;
import kr.startoff.backend.exception.custom.CommentNotFoundException;
import kr.startoff.backend.exception.custom.PostNotFoundException;
import kr.startoff.backend.exception.custom.UserNotFoundException;
import kr.startoff.backend.payload.request.CommentRequest;
import kr.startoff.backend.payload.response.CommentResponse;
import kr.startoff.backend.repository.CommentRepository;
import kr.startoff.backend.repository.PostRepository;
import kr.startoff.backend.repository.UserRepository;
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
