package kr.startoff.backend.domain.comment.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import kr.startoff.backend.global.common.Auditor;
import kr.startoff.backend.domain.post.domain.Post;
import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.domain.comment.dto.CommentRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comment")
@NoArgsConstructor
@Getter
@Setter
public class Comment extends Auditor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	Comment parent;

	@OneToMany(mappedBy = "parent", orphanRemoval = true)
	@Column(name = "child_comments")
	List<Comment> childComments = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	User writer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	Post post;

	@Lob
	@Column(name = "content")
	String content;

	@Column(name = "is_deleted")
	@Enumerated(EnumType.STRING)
	DeleteStatus isDeleted = DeleteStatus.N;

	private void setParent(Comment parent) {
		this.parent = parent;
		parent.getChildComments().add(this);
	}

	public static Comment createComment(User writer, Post post, CommentRequest commentRequest) {
		Comment comment = new Comment();

		comment.setContent(commentRequest.getContent());
		comment.setWriter(writer);
		comment.setPost(post);

		return comment;
	}

	public static Comment createComment(User writer, Post post, Comment parent, CommentRequest commentRequest) {
		Comment comment = new Comment();

		comment.setContent(commentRequest.getContent());
		comment.setWriter(writer);
		comment.setPost(post);
		comment.setParent(parent);

		return comment;
	}

	public void update(CommentRequest commentRequest) {
		this.setContent(commentRequest.getContent());
	}

}
