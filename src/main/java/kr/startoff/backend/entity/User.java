package kr.startoff.backend.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@NoArgsConstructor
@Getter
@Setter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	Long id;

	@Column(name = "password", nullable = false)
	String password;

	@Column(name = "email", nullable = false, unique = true)
	String email;

	@Column(name = "nickname", nullable = false, unique = true)
	String nickname;

	@Column(name = "github_url")
	String githubUrl = "";

	@Column(name = "blog_url")
	String blogUrl = "";

	@Column(name = "baekjoon_id")
	String baekjoonId = "";

	@Column(name = "introduce")
	String introduce = "";

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "provider")
	AuthProvider provider;

	@Column(name = "provider_id")
	String providerId;

	@Column(name = "name")
	String name;

	@Column(name = "image_url")
	String imageUrl;

	@OneToMany(cascade = CascadeType.ALL)
	@JsonIgnore
	List<Project> projects = new ArrayList<>();

	@ManyToMany
	@JoinTable(
		name="user_skill_tag",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name="skill_tag_id"))
	private List<SkillTag> userSkills = new ArrayList<>();

	@Builder
	public User(String email, String nickname, String password, AuthProvider provider) {
		this.email = email;
		this.nickname = nickname;
		this.password = password;
		this.provider = provider;
	}

}
