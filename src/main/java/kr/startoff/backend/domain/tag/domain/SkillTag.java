package kr.startoff.backend.domain.tag.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "skill_tag")
@NoArgsConstructor
@Getter
@Setter
public class SkillTag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "skill_tag_id")
	Long id;

	@Column(name = "skill_name", unique = true)
	String skillName;

	@Column(name = "color", nullable = false)
	String color;

	@Column(name = "text_color", nullable = false)
	String textColor;
}
