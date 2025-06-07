package com.kdev5.cleanpick.customer.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdev5.cleanpick.customer.service.CustomerService;
import com.kdev5.cleanpick.customer.service.dto.request.WriteCustomerRequestDto;
import com.kdev5.cleanpick.customer.service.dto.response.CustomerPrivateResponseDto;
import com.kdev5.cleanpick.global.security.auth.CustomUserDetails;
import com.kdev5.cleanpick.user.domain.Role;
import com.kdev5.cleanpick.user.domain.UserStatus;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.annotation.Resource;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

	@Resource
	private MockMvc mockMvc;

	@Resource
	private CustomerService customerService;

	@Resource
	private ObjectMapper objectMapper;

	private CustomUserDetails mockUserDetails;

	@BeforeEach
	void setupSecurityContext() {
		mockUserDetails = new CustomUserDetails(
			1L, "test@cleanpick.com", "password", Role.CUSTOMER, UserStatus.ACTIVE
		);
		var auth = new UsernamePasswordAuthenticationToken(
			mockUserDetails, null, mockUserDetails.getAuthorities()
		);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@AfterEach
	void clearSecurityContext() {
		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("POST /customers - 최초 고객 등록 성공")
	void writeCustomer_success() throws Exception {
		// given
		String requestJson = """
            {
              "name": "홍길동",
              "phoneNumber": "010-1234-5678",
              "mainAddress": "서울특별시 강남구",
              "subAddress": "역삼동 123-45",
              "profileImageUrl": "https://img.com/user.jpg",
              "latitude": 37.1234,
              "longitude": 127.5678
            }
        """;

		CustomerPrivateResponseDto responseDto = new CustomerPrivateResponseDto(
			"홍길동", "010-1234-5678", "https://img.com/user.jpg",
			"서울특별시 강남구", "역삼동 123-45"
		);

		given(customerService.writeCustomer(eq(1L), any()))
			.willReturn(responseDto);

		// when + then
		mockMvc.perform(post("/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.name").value("홍길동"));
	}

	@Test
	@DisplayName("PUT /customers - 고객 정보 수정 성공")
	void editCustomer_success() throws Exception {
		String requestJson = """
            {
              "name": "김수정",
              "phoneNumber": "010-9876-5432",
              "mainAddress": "서울특별시 서초구",
              "subAddress": "반포동 987-65",
              "profileImageUrl": "https://img.com/edited.jpg",
              "latitude": 37.4321,
              "longitude": 127.8765
            }
        """;

		CustomerPrivateResponseDto responseDto = new CustomerPrivateResponseDto(
			"김수정", "010-9876-5432", "https://img.com/edited.jpg",
			"서울특별시 서초구", "반포동 987-65"
		);

		given(customerService.editCustomer(eq(1L), any()))
			.willReturn(responseDto);

		mockMvc.perform(put("/customers")
				.requestAttr("customerId", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.name").value("김수정"));
	}

	@Test
	@DisplayName("GET /customers - 고객 정보 조회 성공")
	void getCustomer_success() throws Exception {
		CustomerPrivateResponseDto responseDto = new CustomerPrivateResponseDto(
			"이길동", "010-1111-2222", "https://img.com/view.jpg",
			"서울특별시 종로구", "청운동 321-54"
		);

		given(customerService.getCustomer(1L)).willReturn(responseDto);

		mockMvc.perform(get("/customers")
				.requestAttr("customerId", 1L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.name").value("이길동"));
	}

	@TestConfiguration
	static class TestConfig {
		@Bean
		public CustomerService customerService() {
			return Mockito.mock(CustomerService.class);
		}
	}
}
