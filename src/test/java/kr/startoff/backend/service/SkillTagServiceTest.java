package kr.startoff.backend.service;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.startoff.backend.domain.tag.domain.SkillTag;
import kr.startoff.backend.domain.tag.service.SkillTagService;
import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.common.exception.custom.SkillTagBadRequest;
import kr.startoff.backend.common.exception.custom.SkillTagNotFoundException;
import kr.startoff.backend.payload.PayloadFixture;
import kr.startoff.backend.domain.tag.dto.SkillTagResponse;
import kr.startoff.backend.domain.tag.repository.SkillTagRepository;
import kr.startoff.backend.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class SkillTagServiceTest {
	private SkillTagRepository skillTagRepository;
	private UserRepository userRepository;
	private SkillTagService skillTagService;

	@BeforeEach
	private void setUp() {
		skillTagRepository = mock(SkillTagRepository.class);
		userRepository = mock(UserRepository.class);
		skillTagService = new SkillTagService(skillTagRepository, userRepository);
	}

	@Test
	void throwSkillTagNotFoundExceptionTest() throws Exception {
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(getUser()));
		given(skillTagRepository.findBySkillName(SKILL_NAME)).willThrow(SkillTagNotFoundException.class);
		assertThrows(SkillTagNotFoundException.class,
			() -> skillTagService.addSkillTagToUser(USER_ID, SKILL_NAME), "해당 스킬태그를 찾을 수 없습니다.");
	}

	@Test
	void addSkillTagToUserTest() throws Exception {
		User user = getUser();
		SkillTag skillTag = getSkillTag();
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
		given(skillTagRepository.findBySkillName(SKILL_NAME)).willReturn(Optional.of(skillTag));

		skillTagService.addSkillTagToUser(USER_ID, SKILL_NAME);

		assertTrue(user.getUserSkills().contains(skillTag));
	}

	@Test
	void addSkillTagToUserThrowExceptionTest() throws Exception {
		User user = getUser();
		SkillTag skillTag = getSkillTag();
		user.getUserSkills().add(skillTag);
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
		given(skillTagRepository.findBySkillName(SKILL_NAME)).willReturn(Optional.of(skillTag));

		assertThrows(SkillTagBadRequest.class,
			() -> skillTagService.addSkillTagToUser(USER_ID, SKILL_NAME), "이미 존재하는 기술태그입니다.");
	}

	@Test
	void deleteSkillTagToUserTest() throws Exception {
		User user = getUser();
		SkillTag skillTag = getSkillTag();
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
		given(skillTagRepository.findBySkillName(SKILL_NAME)).willReturn(Optional.of(skillTag));
		given(skillTagRepository.findById(SKILL_ID)).willReturn(Optional.of(skillTag));
		skillTagService.addSkillTagToUser(USER_ID, SKILL_NAME);

		skillTagService.deleteSkillTagToUser(USER_ID, SKILL_ID);

		assertFalse(user.getUserSkills().contains(skillTag));
	}

	@Test
	void getAllSkillTagTest() throws Exception {
		given(skillTagRepository.findAll()).willReturn(getSkillTagList());

		List<SkillTagResponse> result = skillTagService.getAllSkillTag();

		for (int i = 0; i < result.size(); i++) {
			assertEquals(result.get(i).getSkillName(), PayloadFixture.SKILLS.get(i));
		}
	}

}