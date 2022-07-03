package kr.startoff.backend.domain.post.domain;

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
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kr.startoff.backend.common.domain.Auditor;
import kr.startoff.backend.domain.tag.domain.SkillTag;
import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.domain.post.dto.PostRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "post")
@NoArgsConstructor
@Getter
@Setter
public class Post extends Auditor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	User author;

	@Column(name = "title", nullable = false)
	String title;

	@Column(name = "category", nullable = false)
	@Enumerated(EnumType.STRING)
	Category category;

	@Lob
	@Column(name = "content")
	String content;

	@Column(name = "current_people")
	Integer currentPeople;

	@Column(name = "max_people")
	Integer maxPeople;

	@ManyToMany
	@JoinTable(
		name = "post_skill_tag",
		joinColumns = @JoinColumn(name = "post_id"),
		inverseJoinColumns = @JoinColumn(name = "skill_tag_id"))
	List<SkillTag> postWantedSkills = new ArrayList<>();

	private void setAuthor(User author) {
		this.author = author;
		author.getUserPosts().add(this);
	}

	public static Post createPost(User author, PostRequest postRequest,List<SkillTag> postWantedSkills) {
		Post post = new Post();

		post.setAuthor(author);
		post.setTitle(postRequest.getTitle());
		post.setCategory(postRequest.getCategory());
		post.setContent(postRequest.getContent());
		post.setMaxPeople(postRequest.getMaxPeople());
		post.setCurrentPeople(postRequest.getCurrentPeople());
		post.setPostWantedSkills(postWantedSkills);
		return post;
	}

	public void updatePost(PostRequest postRequest, List<SkillTag> postWantedSkills) {
		this.setTitle(postRequest.getTitle());
		this.setCategory(postRequest.getCategory());
		this.setContent(postRequest.getContent());
		this.setMaxPeople(postRequest.getMaxPeople());
		this.setCurrentPeople(postRequest.getCurrentPeople());
		this.setPostWantedSkills(postWantedSkills);
	}
}
