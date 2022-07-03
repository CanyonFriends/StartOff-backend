package kr.startoff.backend.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.startoff.backend.domain.project.domain.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
