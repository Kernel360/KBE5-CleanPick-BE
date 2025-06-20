package com.kdev5.cleanpick.review.service;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.exception.ContractException;
import com.kdev5.cleanpick.contract.domain.exception.ContractNotFoundException;
import com.kdev5.cleanpick.contract.infra.ContractRepository;
import com.kdev5.cleanpick.customer.domain.Customer;
import com.kdev5.cleanpick.customer.infra.repository.CustomerRepository;
import com.kdev5.cleanpick.global.exception.ErrorCode;
import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.manager.infra.repository.ManagerRepository;
import com.kdev5.cleanpick.review.Infra.ReviewFileRepository;
import com.kdev5.cleanpick.review.Infra.ReviewRepository;
import com.kdev5.cleanpick.review.domain.Review;
import com.kdev5.cleanpick.review.domain.ReviewFile;
import com.kdev5.cleanpick.review.domain.enumeration.ReviewType;
import com.kdev5.cleanpick.review.domain.exception.ReviewDuplicateException;
import com.kdev5.cleanpick.review.service.dto.request.WriteReviewRequestDto;
import com.kdev5.cleanpick.review.service.dto.response.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewFileRepository reviewFileRepository;
    private final ManagerRepository managerRepository;
    private final CustomerRepository customerRepository;
    private final ContractRepository contractRepository;

    // TODO 로그인 연결
    private final Long userId = 1L;
    private final String userType = "ROLE_MANAGER";


    @Transactional
    public ReviewResponseDto writeReview(WriteReviewRequestDto dto, List<MultipartFile> imgs) {
        Contract contract = contractRepository.findById(dto.getContractId()).orElseThrow(() -> new ContractNotFoundException(ErrorCode.CONTRACT_NOT_FOUND));

        ReviewContext context = resolveReviewer(dto);

        // 리뷰 중복 체크
        if (reviewRepository.findReviewByReviewType(contract, context.customer(), context.manager(), context.reviewType()).isPresent())
            throw new ReviewDuplicateException();

        // 리뷰 저장
        Review review = reviewRepository.save(dto.toEntity(
                context.customer(), context.manager(), contract, context.reviewType()
        ));

        List<String> imgUrls = new ArrayList<>();
        if (imgs != null && !imgs.isEmpty()) {
            // TODO: 이미지 저장 로직 구현
        }

        return ReviewResponseDto.fromEntity(review, imgUrls);
    }

    public Page<ReviewResponseDto> readMyReview(Pageable pageable) {
        Page<Review> reviews = userType.equals("ROLE_USER") ? reviewRepository.findAllReviewByCustomerId(userId, pageable) : reviewRepository.findAllReviewByManagerId(userId, pageable);

        return reviews.map(review -> {
            List<String> fileUrls = reviewFileRepository.findReviewFileByReview(review);
            return ReviewResponseDto.fromEntity(review, fileUrls);
        });
    }

    public List<ReviewResponseDto> readRecentManagerReview() {
        List<Long> ids = reviewRepository.findTopReviewIds();
        List<Review> reviews = reviewRepository.findReviewsWithCustomerManagerAndFiles(ids);

        return reviews.stream()
                .map(review -> {
                    List<String> fileUrls = review.getReviewFiles().stream()
                            .map(ReviewFile::getReviewFileUrl)
                            .collect(Collectors.toList());
                    return ReviewResponseDto.fromEntity(review, fileUrls);
                })
                .collect(Collectors.toList());
    }

    private ReviewContext resolveReviewer(WriteReviewRequestDto dto) {
        //TODO: 추후 user 통합 후 예외처리 변경 필요(baseException)
        if (userType.equals("ROLE_USER")) {
            Manager manager = managerRepository.findById(dto.getTargetId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저입니다."));
            Customer customer = customerRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고객입니다."));
            return new ReviewContext(customer, manager, ReviewType.TO_MANAGER);
        } else if (userType.equals("ROLE_MANAGER")) {
            Manager manager = managerRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저입니다."));
            Customer customer = customerRepository.findById(dto.getTargetId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고객입니다."));
            return new ReviewContext(customer, manager, ReviewType.TO_USER);
        } else {
            throw new IllegalStateException("지원되지 않는 사용자 유형입니다: " + userType);
        }
    }


    private record ReviewContext(Customer customer, Manager manager, ReviewType reviewType) {
    }


}
