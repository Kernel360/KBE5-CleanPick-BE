package com.kdev5.cleanpick.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.kdev5.cleanpick.global.security.auth.CustomUserDetails;
import com.kdev5.cleanpick.manager.service.ManagerService;
import com.kdev5.cleanpick.manager.service.dto.request.ManagerDetailRequestDto;
import com.kdev5.cleanpick.manager.service.dto.response.ManagerPrivateResponseDto;
import com.kdev5.cleanpick.manager.service.dto.response.ManagerSearchResponseDto;
import com.kdev5.cleanpick.user.domain.Role;
import com.kdev5.cleanpick.user.domain.UserStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ManagerController 통합 테스트")
class ManagerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@TestConfiguration
	static class MockConfig {
		@Bean
		public ManagerService managerService() {
			return Mockito.mock(ManagerService.class);
		}
	}

	@Autowired
	private ObjectMapper objectMapper;

	private CustomUserDetails mockUserDetails;

	@Autowired
	private ManagerService managerService;


	@BeforeEach
	void setupAuthentication() {
		mockUserDetails = new CustomUserDetails(
			1L,
			"test@cleanpick.com",
			"encodedPassword",
			Role.MANAGER,
			UserStatus.ACTIVE
		);

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
			mockUserDetails,
			null,
			mockUserDetails.getAuthorities()
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Test
	@DisplayName("GET /manager/search - 매니저 검색 성공")
	void searchManagers() throws Exception {
		ManagerSearchResponseDto dto = new ManagerSearchResponseDto(
			1L,
			"김매니저",
			4.8,
			25,
			"https://image.com/profile.jpg",
			"청소는 제 삶입니다.",
			"서울 강남구",
			List.of("욕실청소", "주방청소")
		);

		Page<ManagerSearchResponseDto> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);

		given(managerService.searchManagers(any(), any(), any(), any(), any()))
			.willReturn(page);

		mockMvc.perform(get("/manager/search")
				.param("cleaning", "욕실청소")
				.param("region", "서울")
				.param("keyword", "김")
				.param("sortType", "RECOMMENDATION")
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.content[0].name").value("김매니저"));
	}

	@Test
	@DisplayName("POST /manager - 매니저 등록 성공")
	@WithMockUser(username = "1", roles = {"MANAGER"})
	void enrollManager() throws Exception {
		ManagerDetailRequestDto request = new ManagerDetailRequestDto();

		String requestJson = """
            {
              "name": "김매니저",
              "phoneNumber": "010-1234-5678",
              "mainAddress": "서울특별시 강남구",
              "subAddress": "논현동 123-45",
              "profileMessage": "청소 자신 있습니다!",
              "profileImageUrl": "https://image.com/profile.jpg",
              "latitude": 37.4979,
              "longitude": 127.0276,
              "availableCleans": [1, 2],
              "availableTimes": [
                { "dayOfWeek": "MONDAY", "startTime": "09:00", "endTime": "12:00" }
              ]
            }
        """;

		ManagerPrivateResponseDto response = new ManagerPrivateResponseDto(
			"김매니저",
			"010-1234-5678",
			"https://image.com/profile.jpg",
			"서울특별시 강남구",
			"논현동 123-45",
			"청소 자신 있습니다!"
		);

		given(managerService.enrollManager(anyLong(), any())).willReturn(response);

		mockMvc.perform(post("/manager")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.name").value("김매니저"));
	}

}
