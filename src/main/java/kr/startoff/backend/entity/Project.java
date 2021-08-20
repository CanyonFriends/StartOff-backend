package kr.startoff.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

	public static Project createProject(User user, ProjectRequest request) {
		Project project = new Project();

		project.setUser(user);
		project.setTitle(request.getTitle());
		project.setIntroduce(request.getIntroduce());
		project.setContent(request.getContent());
		project.setGithubUrl(request.getGithubUrl());
		project.setDeployUrl(request.getDeployUrl());
		project.setStartDate(request.getStartDate());
		project.setEndDate(request.getEndDate());
		return project;
	}

	private void setUser(User user) {
		this.user = user;
		user.getProjects().add(this);
	}

	public void updateProject(ProjectRequest request) {
		this.setTitle(request.getTitle());
		this.setIntroduce(request.getIntroduce());
		this.setContent(request.getContent());
		this.setGithubUrl(request.getGithubUrl());
		this.setDeployUrl(request.getDeployUrl());
		this.setStartDate(request.getStartDate());
		this.setEndDate(request.getEndDate());
	}
}
