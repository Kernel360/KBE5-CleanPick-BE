package com.kdev5.cleanpick.review.service;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.exception.ContractNotFoundException;
import com.kdev5.cleanpick.contract.infra.ContractRepository;
import com.kdev5.cleanpick.customer.domain.Customer;
import com.kdev5.cleanpick.customer.infra.repository.CustomerRepository;
import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.manager.infra.repository.ManagerRepository;
import com.kdev5.cleanpick.review.Infra.ReviewFileRepository;
import com.kdev5.cleanpick.review.Infra.ReviewRepository;
import com.kdev5.cleanpick.review.domain.Review;
import com.kdev5.cleanpick.review.domain.enumeration.ReviewType;
import com.kdev5.cleanpick.review.domain.exception.ReviewDuplicateException;
import com.kdev5.cleanpick.review.service.dto.request.WriteReviewRequestDto;
import com.kdev5.cleanpick.review.service.dto.response.ReviewResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewFileRepository reviewFileRepository;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ContractRepository contractRepository;

    private Contract contract;
    private Customer customer;
    private Manager manager;

    private final Long userId = 1L;
    private final String userType = "ROLE_MANAGER";

    //requestDto
    WriteReviewRequestDto reqDto = createRequestDto();

    private WriteReviewRequestDto createRequestDto() {
        WriteReviewRequestDto dto = new WriteReviewRequestDto();
        ReflectionTestUtils.setField(dto, "targetId", 1L);
        ReflectionTestUtils.setField(dto, "contractId", 1L);
        ReflectionTestUtils.setField(dto, "rating", 4.5F);
        ReflectionTestUtils.setField(dto, "content", "리뷰 내용");
        return dto;
    }


    @BeforeEach
    void setUp() {
        // 기본 테스트용 엔티티 생성
        contract = Contract.builder().build();
        customer = Customer.builder().id(1L).build();
        manager = new Manager();
    }

    @Test
    void 매니저_리뷰작성_성공() {
        // given
        given(contractRepository.findById(any())).willReturn(Optional.of(contract));
        given(managerRepository.findById(1L)).willReturn(Optional.of(manager));
        given(customerRepository.findById(1L)).willReturn(Optional.of(customer));

        // ReviewType 수정
        given(reviewRepository.findReviewByReviewType(eq(contract), eq(customer), eq(manager), eq(ReviewType.TO_USER)))
                .willReturn(Optional.empty());

        Review review = Review.builder()
                .content(reqDto.getContent())
                .rating(reqDto.getRating())
                .contract(contract)
                .customer(customer)
                .manager(manager)
                .type(ReviewType.TO_USER)
                .build();

        given(reviewRepository.save(any(Review.class))).willReturn(review);

        // when
        ReviewResponseDto result = reviewService.writeReview(reqDto, new ArrayList<>());

        // then
        assertThat(result.getTargetName()).isEqualTo(customer.getName());
        assertThat(result.getContent()).isEqualTo(reqDto.getContent());
        assertThat(result.getRating()).isEqualTo(reqDto.getRating());
        assertThat(result.getFiles()).isEmpty();
    }


    @Test
    void 리뷰작성_실패_계약없음() {
        // given
        given(contractRepository.findById(reqDto.getContractId())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reviewService.writeReview(reqDto, null))
                .isInstanceOf(ContractNotFoundException.class);
    }

    @Test
    void 리뷰작성_실패_중복된리뷰() {
        // given
        given(contractRepository.findById(reqDto.getContractId())).willReturn(Optional.of(contract));
        given(managerRepository.findById(reqDto.getTargetId())).willReturn(Optional.of(manager));
        given(customerRepository.findById(1L)).willReturn(Optional.of(customer));
        given(reviewRepository.findReviewByReviewType(eq(contract), eq(customer), eq(manager), eq(ReviewType.TO_USER)))
                .willReturn(Optional.of(Review.builder().build()));

        // when & then
        assertThatThrownBy(() -> reviewService.writeReview(reqDto, null))
                .isInstanceOf(ReviewDuplicateException.class);
    }

    @Test
    void 리뷰작성_실패_매니저없음() {
        // given
        given(contractRepository.findById(reqDto.getContractId())).willReturn(Optional.of(contract));
        given(managerRepository.findById(reqDto.getTargetId())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reviewService.writeReview(reqDto, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 매니저입니다");
    }

    @Test
    void 리뷰작성_실패_고객없음() {
        // given
        given(contractRepository.findById(reqDto.getContractId())).willReturn(Optional.of(contract));
        given(customerRepository.findById(reqDto.getTargetId())).willReturn(Optional.empty());
        given(managerRepository.findById(reqDto.getTargetId())).willReturn(Optional.ofNullable(manager));

        // when & then
        assertThatThrownBy(() -> reviewService.writeReview(reqDto, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 고객입니다");
    }

    @Test
    void 리뷰_조회_성공_ROLE_MANAGER() {
        // given
        Review review = Review.builder().content("좋았어요").customer(customer).rating(5).type(ReviewType.TO_USER).build();
        List<String> fileUrls = List.of("img1.jpg", "img2.jpg");

        Pageable pageable = PageRequest.of(0, 10);


        given(reviewRepository.findAllReviewByManagerId(eq(userId), eq(pageable)))
                .willReturn(new PageImpl<>(List.of(review)));

        given(reviewFileRepository.findReviewFileByReview(review))
                .willReturn(fileUrls);

        // when
        Page<ReviewResponseDto> result = reviewService.readMyReview(pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        ReviewResponseDto dto = result.getContent().get(0);
        assertThat(dto.getContent()).isEqualTo("좋았어요");
        assertThat(dto.getRating()).isEqualTo(5);
        assertThat(dto.getFiles()).containsExactlyElementsOf(fileUrls);
    }
}

