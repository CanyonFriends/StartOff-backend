package kr.startoff.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.startoff.backend.entity.SkillTag;
import kr.startoff.backend.entity.User;
import kr.startoff.backend.exception.custom.SkillTagBadRequest;
import kr.startoff.backend.exception.custom.SkillTagNotFoundException;
import kr.startoff.backend.exception.custom.UserNotFoundException;
import kr.startoff.backend.payload.response.SkillTagResponse;
import kr.startoff.backend.repository.SkillTagRepository;
import kr.startoff.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkillTagService {
	private final SkillTagRepository skillTagRepository;
	private final UserRepository userRepository;

	@Transactional
	public SkillTagResponse addSkillTagToUser(Long userId, String skillName) {
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		SkillTag skillTag = skillTagRepository.findBySkillName(skillName).orElseThrow(SkillTagNotFoundException::new);
		List<SkillTag> userSkills = user.getUserSkills();
		if (userSkills.contains(skillTag)) {
			throw new SkillTagBadRequest("이미 존재하는 기술태그입니다.");
		}
		user.getUserSkills().add(skillTag);
		return new SkillTagResponse(skillTag);
	}

	@Transactional
	public void deleteSkillTagToUser(Long userId, Long skillTagId) {
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		SkillTag skillTag = skillTagRepository.findById(skillTagId).orElseThrow(SkillTagNotFoundException::new);
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
