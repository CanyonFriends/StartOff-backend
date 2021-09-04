package kr.startoff.backend.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
	@Column(name = "parent_comment")
	Comment parentComment;

	@OneToMany(mappedBy = "parentComment",cascade = CascadeType.ALL)
	@Column(name = "child_comments")
	List<Comment> childComments = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@Column(name = "writer")
	User writer;

	@ManyToOne(fetch = FetchType.LAZY)
	@Column(name = "post")
	Post post;

	@Lob
	@Column(name = "content")
	String content;

}
