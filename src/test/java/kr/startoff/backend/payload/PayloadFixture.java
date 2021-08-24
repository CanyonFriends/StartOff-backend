package kr.startoff.backend.payload;

import java.util.List;

import kr.startoff.backend.entity.AuthProvider;
import kr.startoff.backend.entity.Project;
import kr.startoff.backend.entity.SkillTag;
import kr.startoff.backend.entity.User;
import kr.startoff.backend.payload.request.LoginRequest;
import kr.startoff.backend.payload.request.ProjectRequest;
import kr.startoff.backend.payload.request.RefreshOrLogoutRequest;
import kr.startoff.backend.payload.request.SignupRequest;
import kr.startoff.backend.payload.request.UserPasswordChangeRequest;
import kr.startoff.backend.payload.request.profile.BaekjoonIdRequest;
import kr.startoff.backend.payload.request.profile.BlogUrlRequest;
import kr.startoff.backend.payload.request.profile.GithubUrlRequest;
import kr.startoff.backend.payload.request.profile.NicknameAndIntroduceRequest;
import kr.startoff.backend.payload.request.profile.SkillTagRequest;
import kr.startoff.backend.payload.response.AccessTokenResponse;
import kr.startoff.backend.payload.response.CommonResponse;
import kr.startoff.backend.payload.response.LoginResponse;
import kr.startoff.backend.payload.response.ProjectResponse;
import kr.startoff.backend.payload.response.SkillTagResponse;
import kr.startoff.backend.payload.response.UserInfoResponse;
import kr.startoff.backend.payload.response.UserProfileResponse;

public class PayloadFixture {
	public static final String NEW_BLOG_URL = "https://newBlogUrl.blog.com";
	public static final String BAEKJOON_ID = "proto_type";
	public static final String NEW_GITHUB_URL = "https://github.com/protoseo";
	public static final String NICKNAME = "newNickname";
	public static final String INTRODUCE = "안녕하세요. 반갑습니다.";
	public static final String SKILL_NAME = "Spring Boot";
	public static final String EMAIL = "proto_seo@naver.com";
	public static final String PASSWORD = "Password";
	public static final String NEW_PASSWORD = "NEW_PASSWORD";
	public static final String PROJECT_TITLE = "Project Title";
	public static final String PROJECT_INTRODUCE = "Project Introduce";
	public static final String PROJECT_CONTENT = "Project Content";
	public static final String PROJECT_GITHUB_URL = "https://github.com/Start-Off/StartOff-backend";
	public static final String PROJECT_DEPLOY_URL = "https://startoff.kr";
	public static final String PROJECT_START_DATE = "2021-07-26";
	public static final String PROJECT_END_DATE = "2021-09-30";
	public static final List<String> PROJECT_SKILLS = List.of("Spring Boot", "React", "Git", "AWS EC2");
	public static final String UUID = "uuid";
	public static final String ACCESS_TOKEN = "access token";
	public static final Long USER_ID = 1L;
	public static final Long SKILL_ID = 1L;
	public static final Long PROJECT_ID = 1L;

	public static BlogUrlRequest blogUrlRequest() {
		return new BlogUrlRequest(NEW_BLOG_URL);
	}

	public static BaekjoonIdRequest baekjoonIdRequest() {
		return new BaekjoonIdRequest(BAEKJOON_ID);
	}

	public static GithubUrlRequest githubUrlRequest() {
		return new GithubUrlRequest(NEW_GITHUB_URL);
	}

	public static NicknameAndIntroduceRequest nicknameAndIntroduceRequest() {
		return new NicknameAndIntroduceRequest(NICKNAME, INTRODUCE);
	}

	public static SkillTagRequest skillTagRequest() {
		return new SkillTagRequest(SKILL_NAME);
	}

	public static LoginRequest loginRequest() {
		return new LoginRequest(EMAIL, PASSWORD);
	}

	public static ProjectRequest projectRequest() {
		return new ProjectRequest(PROJECT_TITLE, PROJECT_INTRODUCE, PROJECT_CONTENT, PROJECT_GITHUB_URL,
			PROJECT_DEPLOY_URL, PROJECT_START_DATE, PROJECT_END_DATE, PROJECT_SKILLS);
	}

	public static RefreshOrLogoutRequest refreshOrLogoutRequest() {
		return new RefreshOrLogoutRequest(UUID, EMAIL, ACCESS_TOKEN);
	}

	public static SignupRequest signupRequest() {
		return new SignupRequest(EMAIL, NICKNAME, PASSWORD);
	}

	public static UserPasswordChangeRequest passwordChangeRequest() {
		return new UserPasswordChangeRequest(PASSWORD, NEW_PASSWORD);
	}

	public static AccessTokenResponse accessTokenResponse() {
		return new AccessTokenResponse(USER_ID, ACCESS_TOKEN);
	}

	public static CommonResponse commonResponse() {
		return new CommonResponse(true, "성공하였습니다.");
	}

	public static LoginResponse loginResponse() {
		return new LoginResponse(ACCESS_TOKEN, UUID, USER_ID, EMAIL, NICKNAME);
	}

	public static ProjectResponse projectResponse() {
		return new ProjectResponse(getProject());
	}

	public static SkillTagResponse skillTagResponse() {
		return new SkillTagResponse(getSkillTag());
	}

	public static UserInfoResponse userInfoResponse() {
		return new UserInfoResponse(USER_ID, EMAIL, NICKNAME);
	}

	public static UserProfileResponse userProfileResponse() {
		User user = User.builder()
			.email(EMAIL)
			.nickname(NICKNAME)
			.password(PASSWORD).provider(AuthProvider.local)
			.build();
		user.setUserSkills(List.of(getSkillTag()));
		user.setProjects(List.of(getProject()));
		user.setNickname(NICKNAME);
		user.setIntroduce(INTRODUCE);
		user.setGithubUrl(NEW_GITHUB_URL);
		user.setBlogUrl(NEW_BLOG_URL);
		user.setBaekjoonId(BAEKJOON_ID);
		return new UserProfileResponse(user);
	}
	private static SkillTag getSkillTag(){
		SkillTag skillTag = new SkillTag();
		skillTag.setId(SKILL_ID);
		skillTag.setSkillName(SKILL_NAME);
		skillTag.setTextColor("#FFFFFF");
		skillTag.setColor("#000000");
		return skillTag;
	}
	private static Project getProject(){
		Project project = new Project();
		project.setId(PROJECT_ID);
		project.setIntroduce(PROJECT_INTRODUCE);
		project.setTitle(PROJECT_TITLE);
		project.setContent(PROJECT_CONTENT);
		project.setStartDate(PROJECT_START_DATE);
		project.setEndDate(PROJECT_END_DATE);
		project.setGithubUrl(PROJECT_GITHUB_URL);
		project.setDeployUrl(PROJECT_DEPLOY_URL);
		project.setProjectSkills(List.of(getSkillTag()));
		return project;
	}
}