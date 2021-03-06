package kr.startoff.backend.payload;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;

import kr.startoff.backend.domain.user.domain.AuthProvider;
import kr.startoff.backend.domain.post.domain.Category;
import kr.startoff.backend.domain.comment.domain.Comment;
import kr.startoff.backend.domain.post.domain.Post;
import kr.startoff.backend.domain.project.domain.Project;
import kr.startoff.backend.domain.tag.domain.SkillTag;
import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.domain.comment.dto.CommentRequest;
import kr.startoff.backend.domain.user.dto.request.LoginRequest;
import kr.startoff.backend.domain.post.dto.PostRequest;
import kr.startoff.backend.domain.project.dto.ProjectRequest;
import kr.startoff.backend.domain.user.dto.request.RefreshOrLogoutRequest;
import kr.startoff.backend.domain.user.dto.request.SignupRequest;
import kr.startoff.backend.domain.user.dto.request.UserPasswordChangeRequest;
import kr.startoff.backend.domain.user.dto.request.profile.BaekjoonIdRequest;
import kr.startoff.backend.domain.user.dto.request.profile.BlogUrlRequest;
import kr.startoff.backend.domain.user.dto.request.profile.GithubUrlRequest;
import kr.startoff.backend.domain.user.dto.request.profile.NicknameAndIntroduceRequest;
import kr.startoff.backend.domain.user.dto.request.profile.SkillTagRequest;
import kr.startoff.backend.domain.user.dto.response.AccessTokenResponse;
import kr.startoff.backend.domain.comment.dto.CommentResponse;
import kr.startoff.backend.global.common.dto.CommonResponse;
import kr.startoff.backend.domain.user.dto.response.LoginResponse;
import kr.startoff.backend.domain.post.dto.PostListResponse;
import kr.startoff.backend.domain.post.dto.PostResponse;
import kr.startoff.backend.domain.project.dto.ProjectResponse;
import kr.startoff.backend.domain.tag.dto.SkillTagResponse;
import kr.startoff.backend.domain.user.dto.response.UserInfoResponse;
import kr.startoff.backend.domain.user.dto.response.UserProfileResponse;

public class PayloadFixture {
	public static final String NEW_BLOG_URL = "https://newBlogUrl.blog.com";
	public static final String BAEKJOON_ID = "proto_type";
	public static final String NEW_GITHUB_URL = "https://github.com/protoseo";
	public static final String IMAGE_URL = "https://bucket.s3.ap-northeast-2.amazonaws.com/1/profile.jpeg";
	public static final String NICKNAME = "Nickname";
	public static final String NEW_NICKNAME = "newNickname";
	public static final String INTRODUCE = "Introduce";
	public static final String SKILL_NAME = "Spring Boot";
	public static final String EMAIL = "proto_seo@naver.com";
	public static final String NEW_EMAIL = "nexon_dog@naver.com";
	public static final String PASSWORD = "Password";
	public static final String NEW_PASSWORD = "NEW_PASSWORD";
	public static final String TITLE = "Title";
	public static final String CONTENT = "Content";
	public static final String UPDATE_TITLE = "UPDATE Title";
	public static final String UPDATE_INTRODUCE = "UPDATE Introduce";
	public static final String UPDATE_CONTENT = "UPDATE Content";
	public static final String PROJECT_GITHUB_URL = "https://github.com/Start-Off/StartOff-backend";
	public static final String PROJECT_DEPLOY_URL = "https://startoff.kr";
	public static final String PROJECT_START_DATE = "2021-07-26";
	public static final String PROJECT_END_DATE = "2021-09-30";
	public static final List<String> SKILLS = List.of("Spring Boot", "React", "Git", "AWS EC2");
	public static final String UUID = "uuid";
	public static final String ACCESS_TOKEN = "access token";
	public static final String NEW_ACCESS_TOKEN = "new access token";
	public static final String REFRESH_TOKEN = "refresh token";
	public static final Long USER_ID = 1L;
	public static final Long SKILL_ID = 1L;
	public static final Long PROJECT_ID = 1L;
	public static final Long PARENT_ID = 1L;
	public static final Long CHILD_ID = 2L;
	public static final Long POST_ID = 1L;
	public static final Category CATEGORY = Category.PROJECT;
	public static final Integer CURRENT_PEOPLE = 1;
	public static final Integer MAX_PEOPLE = 4;
	public static final LocalDateTime now = LocalDateTime.of(2021, 9, 12, 12, 32, 10);

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
		return new NicknameAndIntroduceRequest(NEW_NICKNAME, INTRODUCE);
	}

	public static NicknameAndIntroduceRequest introduceRequest() {
		return new NicknameAndIntroduceRequest(NICKNAME, INTRODUCE);
	}

	public static SkillTagRequest skillTagRequest() {
		return new SkillTagRequest(SKILL_NAME);
	}

	public static LoginRequest loginRequest() {
		return new LoginRequest(EMAIL, PASSWORD);
	}

	public static ProjectRequest projectRequest() {
		return new ProjectRequest(TITLE, INTRODUCE, CONTENT, PROJECT_GITHUB_URL,
			PROJECT_DEPLOY_URL, PROJECT_START_DATE, PROJECT_END_DATE, SKILLS);
	}

	public static ProjectRequest updateProjectRequest() {
		return new ProjectRequest(UPDATE_TITLE, UPDATE_INTRODUCE, UPDATE_CONTENT,
			PROJECT_GITHUB_URL, PROJECT_DEPLOY_URL, PROJECT_START_DATE, PROJECT_END_DATE, SKILLS);
	}

	public static ProjectResponse updateProjectResponse() {
		return new ProjectResponse(getUpdateProject());
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

	public static AccessTokenResponse newAccessTokenResponse() {
		return new AccessTokenResponse(USER_ID, NEW_ACCESS_TOKEN);
	}

	public static CommonResponse commonResponse(String msg) {
		return new CommonResponse(true, msg);
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
		return new UserProfileResponse(getUserForProfile());
	}

	public static SkillTag getSkillTag() {
		SkillTag skillTag = new SkillTag();
		skillTag.setId(SKILL_ID);
		skillTag.setSkillName(SKILL_NAME);
		skillTag.setTextColor("#FFFFFF");
		skillTag.setColor("#000000");
		return skillTag;
	}

	public static SkillTag getSkillTag(Long skillId, String skillName) {
		SkillTag skillTag = new SkillTag();
		skillTag.setId(skillId);
		skillTag.setSkillName(skillName);
		skillTag.setTextColor("#FFFFFF");
		skillTag.setColor("#000000");
		return skillTag;
	}

	public static List<SkillTag> getSkillTagList() {
		List<SkillTag> skillTags = new ArrayList<>();
		Long id = 1L;
		for (String userSkill : SKILLS) {
			skillTags.add(getSkillTag(id++, userSkill));
		}
		return skillTags;
	}

	public static Project getProject() {
		Project project = new Project();
		project.setId(PROJECT_ID);
		project.setIntroduce(INTRODUCE);
		project.setTitle(TITLE);
		project.setContent(CONTENT);
		project.setStartDate(PROJECT_START_DATE);
		project.setEndDate(PROJECT_END_DATE);
		project.setGithubUrl(PROJECT_GITHUB_URL);
		project.setDeployUrl(PROJECT_DEPLOY_URL);
		project.setProjectSkills(getSkillTagList());
		return project;
	}

	public static Project getUpdateProject() {
		Project project = new Project();
		project.setId(PROJECT_ID);
		project.setIntroduce(UPDATE_INTRODUCE);
		project.setTitle(UPDATE_TITLE);
		project.setContent(UPDATE_CONTENT);
		project.setStartDate(PROJECT_START_DATE);
		project.setEndDate(PROJECT_END_DATE);
		project.setGithubUrl(PROJECT_GITHUB_URL);
		project.setDeployUrl(PROJECT_DEPLOY_URL);
		project.setProjectSkills(getSkillTagList());
		return project;
	}

	public static Project getProject(User user) {
		Project project = Project.createProject(user, projectRequest(), getSkillTagList());
		return project;
	}

	public static User getUser(PasswordEncoder encoder) {
		return User.builder()
			.email(EMAIL)
			.nickname(NICKNAME)
			.password(encoder.encode(PASSWORD))
			.provider(AuthProvider.local)
			.build();
	}

	public static User getUser() {
		User user = User.builder()
			.email(EMAIL)
			.nickname(NICKNAME)
			.password(PASSWORD)
			.provider(AuthProvider.local)
			.build();
		user.setId(USER_ID);
		return user;
	}

	public static User getUserForProfile() {
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
		user.setImageUrl(IMAGE_URL);
		user.setBaekjoonId(BAEKJOON_ID);
		return user;
	}

	public static CommentRequest parentCommentRequest() {
		return new CommentRequest(USER_ID, null, CONTENT);
	}

	public static CommentRequest updateCommentRequest() {
		return new CommentRequest(USER_ID, null, UPDATE_CONTENT);
	}

	public static CommentRequest childCommentRequest() {
		return new CommentRequest(USER_ID, PARENT_ID, CONTENT);
	}

	public static PostRequest postRequest() {
		return new PostRequest(USER_ID, TITLE, CONTENT, CATEGORY, SKILLS, CURRENT_PEOPLE, MAX_PEOPLE);
	}

	public static PostRequest updatePostRequest() {
		return new PostRequest(USER_ID, UPDATE_TITLE, UPDATE_CONTENT, CATEGORY, SKILLS, CURRENT_PEOPLE, MAX_PEOPLE);
	}

	public static PostResponse postResponse() {
		return new PostResponse(getPost(postRequest()));
	}

	public static PostResponse updatePostResponse() {
		return new PostResponse(getPost(updatePostRequest()));
	}

	public static PostResponse postResponseSetComment() {
		PostResponse postResponse = new PostResponse(getPost(postRequest()));
		postResponse.setComments(List.of(parentCommentResponse(childCommentResponse())));
		return postResponse;
	}

	public static PostListResponse postListResponse() {
		return new PostListResponse(getPost(postRequest()));
	}

	public static CommentResponse parentCommentResponse(CommentResponse commentResponse) {
		CommentResponse parentCommentResponse = new CommentResponse(getParentComment());
		parentCommentResponse.addChildComment(commentResponse);
		return parentCommentResponse;
	}

	public static CommentResponse parentCommentResponse() {
		return new CommentResponse(getParentComment());
	}

	public static CommentResponse childCommentResponse() {
		return new CommentResponse(getChildComment());
	}

	public static CommentResponse updateCommentResponse() {
		Comment comment = Comment.createComment(getUser(), getPost(postRequest()), parentCommentRequest());
		comment.setId(PARENT_ID);
		comment.setCreatedAt(now);
		comment.update(updateCommentRequest());
		return new CommentResponse(comment);
	}

	public static Post getPost(PostRequest postRequest) {
		Post post = Post.createPost(getUser(), postRequest, getSkillTagList());
		post.setId(POST_ID);
		post.setCreatedAt(now);
		return post;
	}

	public static Comment getParentComment() {
		Comment comment = Comment.createComment(getUser(), getPost(postRequest()), parentCommentRequest());
		comment.setId(PARENT_ID);
		comment.setCreatedAt(now);
		return comment;
	}

	public static Comment getChildComment() {
		Comment comment = Comment.createComment(getUser(), getPost(postRequest()), getParentComment(),
			childCommentRequest());
		comment.setId(CHILD_ID);
		comment.setCreatedAt(now);
		return comment;
	}
}
