package kr.startoff.backend.domain.tag.service;

import static kr.startoff.backend.global.exception.ExceptionType.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.startoff.backend.domain.tag.domain.SkillTag;
import kr.startoff.backend.domain.tag.exception.TagException;
import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.domain.user.exception.UserException;
import kr.startoff.backend.domain.tag.dto.SkillTagResponse;
import kr.startoff.backend.domain.tag.repository.SkillTagRepository;
import kr.startoff.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkillTagService {
	private final SkillTagRepository skillTagRepository;
	private final UserRepository userRepository;

	@Transactional
	public SkillTagResponse addSkillTagToUser(Long userId, String skillName) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));
		SkillTag skillTag = skillTagRepository.findBySkillName(skillName)
			.orElseThrow(() -> new TagException(TAG_NOT_FOUND));
		List<SkillTag> userSkills = user.getUserSkills();
		if (userSkills.contains(skillTag)) {
			throw new TagException(TAG_CONFLICT);
		}
		user.getUserSkills().add(skillTag);
		return new SkillTagResponse(skillTag);
	}

	@Transactional
	public void deleteSkillTagToUser(Long userId, Long skillTagId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));
		SkillTag skillTag = skillTagRepository.findById(skillTagId).orElseThrow(() -> new TagException(TAG_NOT_FOUND));
		user.getUserSkills().remove(skillTag);
	}

	@Transactional(readOnly = true)
	public List<SkillTagResponse> getAllSkillTag() {
		return skillTagRepository.findAll()
			.stream()
			.map(SkillTagResponse::new)
			.collect(Collectors.toList());
	}

}
