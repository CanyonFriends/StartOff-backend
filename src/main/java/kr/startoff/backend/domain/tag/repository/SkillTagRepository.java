package kr.startoff.backend.domain.tag.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.startoff.backend.domain.tag.domain.SkillTag;

public interface SkillTagRepository extends JpaRepository<SkillTag, Long> {
	Optional<SkillTag> findBySkillName(String skillName);
}
