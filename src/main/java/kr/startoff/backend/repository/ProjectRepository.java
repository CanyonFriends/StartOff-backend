package kr.startoff.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.startoff.backend.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
