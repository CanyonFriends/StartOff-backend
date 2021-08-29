package kr.startoff.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.startoff.backend.entity.SkillTag;

public interface SkillTagRepository extends JpaRepository<SkillTag, Long> {
	Optional<SkillTag> findBySkillName(String skillName);
}
