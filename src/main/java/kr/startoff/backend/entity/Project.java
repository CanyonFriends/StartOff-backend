package kr.startoff.backend.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kr.startoff.backend.payload.request.ProjectRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "project")
@NoArgsConstructor
@Getter
@Setter
public class Project {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "project_id")
	Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	User user;

	@Column(name = "title", nullable = false)
	String title;

	@Column(name = "introduce")
	String introduce = "";

	@Column(name = "content")
	String content = "";

	@Column(name = "github_url")
	String githubUrl = "";

	@Column(name = "deploy_url")
	String deployUrl = "";

	@Column(name = "start_date")
	String startDate;

	@Column(name = "end_date")
	String endDate;

	@ManyToMany
	@JoinTable(
		name = "project_skill_tag",
		joinColumns = @JoinColumn(name = "project_id"),
		inverseJoinColumns = @JoinColumn(name = "skill_tag_id"))
	private List<SkillTag> projectSkills = new ArrayList<>();

	public static Project createProject(User user, ProjectRequest projectRequest,List<SkillTag> projectSkills) {
		Project project = new Project();

		project.setUser(user);
		project.setTitle(projectRequest.getTitle());
		project.setIntroduce(projectRequest.getIntroduce());
		project.setContent(projectRequest.getContent());
		project.setGithubUrl(projectRequest.getGithubUrl());
		project.setDeployUrl(projectRequest.getDeployUrl());
		project.setStartDate(projectRequest.getStartDate());
		project.setEndDate(projectRequest.getEndDate());
		project.setProjectSkills(projectSkills);
		return project;
	}

	private void setUser(User user) {
		this.user = user;
		user.getProjects().add(this);
	}

	public void updateProject(ProjectRequest projectRequest, List<SkillTag> projectSkills) {
		this.setTitle(projectRequest.getTitle());
		this.setIntroduce(projectRequest.getIntroduce());
		this.setContent(projectRequest.getContent());
		this.setGithubUrl(projectRequest.getGithubUrl());
		this.setDeployUrl(projectRequest.getDeployUrl());
		this.setStartDate(projectRequest.getStartDate());
		this.setEndDate(projectRequest.getEndDate());
		this.setProjectSkills(projectSkills);
	}
}
