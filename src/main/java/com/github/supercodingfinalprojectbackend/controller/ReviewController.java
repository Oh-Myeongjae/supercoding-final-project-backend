package com.github.supercodingfinalprojectbackend.controller;

import com.github.supercodingfinalprojectbackend.dto.ReviewDto;
import com.github.supercodingfinalprojectbackend.dto.Reviewable;
import com.github.supercodingfinalprojectbackend.service.ReviewService;
import com.github.supercodingfinalprojectbackend.util.ResponseUtils;
import com.github.supercodingfinalprojectbackend.util.ResponseUtils.ApiResponse;
import com.github.supercodingfinalprojectbackend.util.auth.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.github.supercodingfinalprojectbackend.dto.ReviewDto.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateReviewResponse>> createReview(
            @RequestBody @Valid CreateReviewRequest request
    ) {
            Long userId = AuthUtils.getUserId();
            return ResponseUtils.created(
                    "리뷰 작성을 성공하였습니다.",
                    ReviewDto.CreateReviewResponse.from(
                            reviewService.createReview(request, userId)
                    )
            );
    }

    @GetMapping("/byPostId")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getReviews(
            @RequestParam Long postId,
            @RequestParam(defaultValue = "0") Long cursor,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
            return ResponseUtils.ok(
                    "포스트에 대한 리뷰 조회를 성공하였습니다.",
                    reviewService.getReviews(postId, cursor, PageRequest.of(0, pageSize))
                            .map(ReviewDto.ReviewResponse::from)
            );
    }

    @GetMapping("/byUserId")
    public ResponseEntity<ApiResponse<Page<MyReviewResponse>>> getMyReviews(
            @RequestParam(defaultValue = "0") Long cursor,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
            Long userId = AuthUtils.getUserId();
            return ResponseUtils.ok(
                    "user가 작성한 리뷰 조회를 성공하였습니다.",
                    reviewService.getMyReviews(userId, cursor, PageRequest.of(0, pageSize))
                            .map(ReviewDto.MyReviewResponse::from)
            );
    }

    @GetMapping("/reviewable")
    public ResponseEntity<ApiResponse<Page<Reviewable>>> getReviewableReviews(
            @RequestParam(defaultValue = "0") Long cursor,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
            Long userId = AuthUtils.getUserId();
            return ResponseUtils.ok(
                    "user가 리뷰 가능한 리뷰 목록 조회를 성공하였습니다.",
                    reviewService.getReviewableReviews(userId, cursor, PageRequest.of(0, pageSize))
            );
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> deleteReview(
            @PathVariable("reviewId") Long reviewId
    ) {
            Long userId = AuthUtils.getUserId();
            return ResponseUtils.ok(
                    "리뷰 삭제를 성공하였습니다.",
                    ReviewDto.ReviewResponse.from(
                            reviewService.deleteReview(userId, reviewId))
            );
    }
}
