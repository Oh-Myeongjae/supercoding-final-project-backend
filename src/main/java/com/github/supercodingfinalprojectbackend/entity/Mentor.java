package com.github.supercodingfinalprojectbackend.entity;

import com.github.supercodingfinalprojectbackend.dto.MentorCareerDto;
import com.github.supercodingfinalprojectbackend.dto.MentorDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

	@Column(name = "star")
	private Float star;

	public static Mentor of(User user, MentorDto mentorDto) {
		return Mentor.builder()
				.user(user)
				.introduction(mentorDto.getIntroduction())
				.searchable(false)
				.company(mentorDto.getCompany())
				.currentDuty(mentorDto.getCurrentDuty().resolve().name())
				.currentPeriod(mentorDto.getCurrentPeriod())
				.build();
	}

	public static Mentor of(User user, String company, String introduction) {
		return Mentor.builder()
				.user(Objects.requireNonNull(user))
				.company(company)
				.introduction(introduction)
				.searchable(false)
				.build();
	}

	public void setMentorSkillStacks(List<MentorSkillStack> mentorSkillStacks) { this.mentorSkillStacks = mentorSkillStacks; }

	@Override
	public boolean isValid() {
		return !this.getIsDeleted() && this.getSearchable();
	}

	public void changeInfo(String introduction, String company, List<MentorSkillStack> mentorSkillStacks, List<MentorCareer> mentorCareers, Boolean searchable) {
		if (introduction != null) this.introduction = introduction;
		if (company != null) this.company = company;
		if (mentorSkillStacks != null) this.mentorSkillStacks = mentorSkillStacks;
		if (mentorCareers != null) this.mentorCareerList = mentorCareers;
		if (searchable != null) this.searchable = searchable;
	}

	public void setMentorCareers(List<MentorCareer> mentorCareers) {
		if (mentorCareers != null && !mentorCareers.isEmpty()) {
			this.currentDuty = mentorCareers.get(0).getDuty();
			this.currentPeriod = mentorCareers.get(0).getDuty();
		}
		this.mentorCareerList = mentorCareers;
	}

	public void changeInfo(MentorDto.ChangeInfoRequest request) {
		this.user.changeInfo(request.getNickname(), request.getEmail(), request.getThumbnailImageUrl());
		this.introduction = request.getIntroduction();
		this.company = request.getCompany();
		this.searchable = request.getSearchable();
		this.currentDuty = request.getCurrentDuty();
		this.currentPeriod = request.getCurrentPeriod();
	}

	public void updateStar(Float star) {
		this.star = star;
	}
}
