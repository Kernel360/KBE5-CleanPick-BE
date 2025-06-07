package com.kdev5.cleanpick.customer.service;

import com.kdev5.cleanpick.customer.domain.Customer;

import com.kdev5.cleanpick.customer.domain.exception.CustomerNotFoundException;
import com.kdev5.cleanpick.customer.infra.repository.CustomerRepository;
import com.kdev5.cleanpick.customer.service.dto.request.WriteCustomerRequestDto;
import com.kdev5.cleanpick.customer.service.dto.response.CustomerPrivateResponseDto;

import com.kdev5.cleanpick.user.domain.Role;
import com.kdev5.cleanpick.user.domain.User;
import com.kdev5.cleanpick.user.domain.UserStatus;
import com.kdev5.cleanpick.user.domain.exception.UserNotFoundException;
import com.kdev5.cleanpick.user.infra.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class CustomerServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private CustomerRepository customerRepository;

	@InjectMocks
	private CustomerService customerService;

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("writeCustomer - 고객 최초 등록 성공")
	void writeCustomer_success() {
		// given
		Long userId = 1L;

		User user = User.reference(userId);
		WriteCustomerRequestDto requestDto = makeRequestDto();

		Customer savedCustomer = requestDto.toEntity(user);
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(customerRepository.save(any())).thenReturn(savedCustomer);

		// when
		CustomerPrivateResponseDto result = customerService.writeCustomer(userId, requestDto);

		// then
		assertThat(result.getName()).isEqualTo("홍길동");
		verify(userRepository).findById(userId);
		verify(customerRepository).save(any(Customer.class));
	}

	@Test
	@DisplayName("writeCustomer - 존재하지 않는 사용자 예외")
	void writeCustomer_userNotFound() {
		// given
		Long userId = 99L;
		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		// when + then
		assertThatThrownBy(() -> customerService.writeCustomer(userId, makeRequestDto()))
			.isInstanceOf(UserNotFoundException.class);
	}

	@Test
	@DisplayName("editCustomer - 고객 정보 수정 성공")
	void editCustomer_success() {
		// given
		Long customerId = 1L;
		WriteCustomerRequestDto requestDto = makeRequestDto();

		Customer customer = requestDto.toEntity(User.reference(customerId));
		when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

		// when
		CustomerPrivateResponseDto result = customerService.editCustomer(customerId, requestDto);

		// then
		assertThat(result.getName()).isEqualTo("홍길동");
		verify(customerRepository).findById(customerId);
	}

	@Test
	@DisplayName("editCustomer - 고객 없음 예외")
	void editCustomer_notFound() {
		when(customerRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> customerService.editCustomer(1L, makeRequestDto()))
			.isInstanceOf(CustomerNotFoundException.class);
	}

	@Test
	@DisplayName("getCustomer - 고객 조회 성공")
	void getCustomer_success() {
		Long customerId = 1L;
		Customer customer = makeRequestDto().toEntity(User.reference(customerId));

		when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

		CustomerPrivateResponseDto result = customerService.getCustomer(customerId);

		assertThat(result.getPhoneNumber()).isEqualTo("010-1234-5678");
	}

	@Test
	@DisplayName("getCustomer - 고객 없음 예외")
	void getCustomer_notFound() {
		when(customerRepository.findById(2L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> customerService.getCustomer(2L))
			.isInstanceOf(CustomerNotFoundException.class);
	}

	// 🧪 공통 생성 메서드
	private WriteCustomerRequestDto makeRequestDto() {
		WriteCustomerRequestDto dto = new WriteCustomerRequestDto();
		// 리플렉션으로 채우기 (Setter 없기 때문에)
		setField(dto, "name", "홍길동");
		setField(dto, "phoneNumber", "010-1234-5678");
		setField(dto, "mainAddress", "서울시 강남구");
		setField(dto, "subAddress", "역삼동 123");
		setField(dto, "profileImageUrl", "https://img.com/user.jpg");
		setField(dto, "latitude", 37.0);
		setField(dto, "longitude", 127.0);
		return dto;
	}

	private void setField(Object target, String field, Object value) {
		try {
			var f = target.getClass().getDeclaredField(field);
			f.setAccessible(true);
			f.set(target, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
