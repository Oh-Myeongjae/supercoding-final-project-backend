package com.github.supercodingfinalprojectbackend.service;

import com.github.supercodingfinalprojectbackend.dto.MentorDto;
import com.github.supercodingfinalprojectbackend.dto.MentorDto.MentorInfoResponse;
import com.github.supercodingfinalprojectbackend.entity.Mentor;
import com.github.supercodingfinalprojectbackend.exception.ApiException;
import com.github.supercodingfinalprojectbackend.repository.MentorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.github.supercodingfinalprojectbackend.exception.errorcode.ApiErrorCode.NOT_FOUND_MENTOR;

@Slf4j
@RequiredArgsConstructor
@Service
public class MentorService {

	private final MentorRepository mentorRepository;


	public Page<MentorDto.MentorInfoResponse> getMentors(
			String keyWord, List<String> skillStacks, Long cursor, Pageable pageable){

//		1차 구현 순수 Entity 조회
//		List<Mentor> mentors = mentorRepository.searchAll(keyWord, skillStacks);

//		2차 구현 DTO 조회
//		List<MentorDto.MentorInfoResponse> mentors = mentorRepository.searchAllFromDto(keyWord, skillStacks);

//		3차 OffsetPagination
//		Page<MentorInfoResponse> mentors = mentorRepository.searchAllFromDtoWithOffsetPagination(keyWord, skillStacks, pageable);

//		4차 cursorPagination
		Page<MentorInfoResponse> mentors = mentorRepository.searchAllFromDtoWithCursorPagination(keyWord, skillStacks, cursor, pageable);

//		1차 응답
//		return mentors.stream().map(MentorDto::fromEntity).collect(Collectors.toList());
		return mentors;
	}

	public MentorDto getMentorDetail(Long mentorId) {
		Mentor mentor = mentorRepository.findById(mentorId)
				.orElseThrow(() -> new ApiException(NOT_FOUND_MENTOR));

		if (!mentor.isValid()) {
			throw new ApiException(NOT_FOUND_MENTOR);
		}
		return MentorDto.from2(mentor);
	}
}
