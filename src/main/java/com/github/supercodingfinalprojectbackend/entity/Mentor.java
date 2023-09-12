package com.github.supercodingfinalprojectbackend.entity;

import com.github.supercodingfinalprojectbackend.dto.MentorDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mentors")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "mentorId", callSuper = false)
@Builder
public class Mentor extends CommonEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mentor_id", nullable = false)
	private Long mentorId;

	@OneToMany(mappedBy = "mentor")
	private List<MentorSkillStack> mentorSkillStacks = new ArrayList<>();

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "introduction")
	private String introduction;

	@Column(name = "company")
	private String company;

	@Column(name = "searchable")
	private Boolean searchable;

	@Column(name = "current_duty")
	private String currentDuty;

	@Column(name = "current_period")
	private String currentPeriod;

	@OneToMany(mappedBy = "mentor")
	private List<MentorCareer> mentorCareerList = new ArrayList<>();

	public static Mentor from(User user, MentorDto mentorDto) {
		return Mentor.builder()
				.mentorSkillStacks(null)
				.user(user)
				.introduction(mentorDto.getIntroduction())
				.searchable(false)
				.company(mentorDto.getCompany())
				.currentDuty(mentorDto.getCurrentDuty().resolve().name())
				.currentPeriod(mentorDto.getCurrentPeriod())
				.build();
	}

	public void setMentorSkillStacks(List<MentorSkillStack> mentorSkillStacks) { this.mentorSkillStacks = mentorSkillStacks; }

	@Override
	public boolean isValid() {
		return !this.getIsDeleted() && this.getSearchable();
	}
}
