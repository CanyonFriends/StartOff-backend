package kr.startoff.backend.service;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.entity.Comment;
import kr.startoff.backend.entity.DeleteStatus;
import kr.startoff.backend.exception.custom.CommentNotFoundException;
import kr.startoff.backend.payload.request.CommentRequest;
import kr.startoff.backend.payload.response.CommentResponse;
import kr.startoff.backend.repository.CommentRepository;
import kr.startoff.backend.repository.PostRepository;
import kr.startoff.backend.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
	private CommentRepository commentRepository;
	private UserRepository userRepository;
	private PostRepository postRepository;
	private CommentService commentService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		commentRepository = mock(CommentRepository.class);
		userRepository = mock(UserRepository.class);
		postRepository = mock(PostRepository.class);
		commentService = new CommentService(commentRepository, userRepository, postRepository);
	}

	@Test
	void saveChildCommentTest() throws Exception {
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(getUser()));
		given(postRepository.findById(POST_ID)).willReturn(Optional.of(getPost(postRequest())));
		given(commentRepository.findById(PARENT_ID)).willReturn(Optional.of(getParentComment()));
		given(commentRepository.save(any())).willReturn(getChildComment());

		CommentResponse childCommentResponse = commentService.saveComment(POST_ID, childCommentRequest());

		assertEquals(objectMapper.writeValueAsString(childCommentResponse),
			objectMapper.writeValueAsString(childCommentResponse()));
	}

	@Test
	void saveChildCommentThrowExceptionTest() throws Exception {
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(getUser()));
		given(postRepository.findById(POST_ID)).willReturn(Optional.of(getPost(postRequest())));
		given(commentRepository.findById(PARENT_ID)).willThrow(CommentNotFoundException.class);
		final CommentRequest childCommentRequest = childCommentRequest();

		assertThrows(CommentNotFoundException.class,
			() -> commentService.saveComment(POST_ID, childCommentRequest), "해당 댓글이 존재하지 않습니다.");
	}

	@Test
	void saveParentCommentTest() throws Exception {
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(getUser()));
		given(postRepository.findById(POST_ID)).willReturn(Optional.of(getPost(postRequest())));
		given(commentRepository.save(any())).willReturn(getParentComment());

		CommentResponse parentCommentResponse = commentService.saveComment(POST_ID, parentCommentRequest());

		assertEquals(objectMapper.writeValueAsString(parentCommentResponse),
			objectMapper.writeValueAsString(parentCommentResponse()));
	}

	@Test
	void updateCommentTest() throws Exception {
		given(commentRepository.findById(PARENT_ID)).willReturn(Optional.of(getParentComment()));

		CommentResponse commentResponse = commentService.updateComment(PARENT_ID, updateCommentRequest());

		assertEquals(objectMapper.writeValueAsString(commentResponse),
			objectMapper.writeValueAsString(updateCommentResponse()));
	}

	@Test
	void updateCommentThrowExceptionTest() throws Exception {
		given(commentRepository.findById(PARENT_ID)).willThrow(CommentNotFoundException.class);
		final CommentRequest updateCommentRequest = updateCommentRequest();

		assertThrows(CommentNotFoundException.class,
			() -> commentService.updateComment(PARENT_ID, updateCommentRequest), "해당 댓글이 존재하지 않습니다.");
	}

	@Test
	void deleteParentWithNotChildComment() throws Exception {
		given(commentRepository.findWithParentById(PARENT_ID)).willReturn(Optional.of(getParentComment()));

		Long id = commentService.deleteComment(PARENT_ID);

		assertEquals(PARENT_ID, id);
	}

	@Test
	void deleteParentWithChildComment() throws Exception {
		Comment parent = getTestParentComment();
		given(commentRepository.findWithParentById(PARENT_ID)).willReturn(Optional.of(parent));

		Long id = commentService.deleteComment(PARENT_ID);
		assertEquals(DeleteStatus.Y, parent.getIsDeleted());
		assertEquals(PARENT_ID, id);
	}

	@Test
	void deleteGrandsonWithGrandsonComment() throws Exception {
		Comment parent = getTestParentComment();
		Comment child = parent.getChildComments().get(0);
		Comment grandson = child.getChildComments().get(0);
		given(commentRepository.findWithParentById(child.getId())).willReturn(Optional.of(child));
		given(commentRepository.findWithParentById(grandson.getId())).willReturn(Optional.of(grandson));

		commentService.deleteComment(child.getId());
		Long id = commentService.deleteComment(grandson.getId());
		assertEquals(4L, id);
	}

	@Test
	void deleteChildWithGrandsonComment() throws Exception {
		Comment parent = getTestParentComment();
		Comment child = parent.getChildComments().get(0);
		given(commentRepository.findWithParentById(child.getId())).willReturn(Optional.of(child));

		Long id = commentService.deleteComment(child.getId());
		assertEquals(CHILD_ID, id);
	}

	private Comment getTestParentComment() {
		Comment parent = Comment.createComment(getUser(), getPost(postRequest()), parentCommentRequest());
		parent.setId(PARENT_ID);
		parent.setCreatedAt(LocalDateTime.now());

		Comment child1 = Comment.createComment(getUser(), getPost(postRequest()), parent, parentCommentRequest());
		child1.setId(CHILD_ID);
		child1.setCreatedAt(LocalDateTime.now());

		Comment grandson = Comment.createComment(getUser(), getPost(postRequest()), child1, parentCommentRequest());
		grandson.setId(4L);
		grandson.setCreatedAt(LocalDateTime.now());
		child1.setChildComments(List.of(grandson));

		Comment child2 = Comment.createComment(getUser(), getPost(postRequest()), parent, parentCommentRequest());
		child2.setId(3L);
		child2.setCreatedAt(LocalDateTime.now());

		parent.setChildComments(List.of(child1, child2));
		return parent;
	}
}