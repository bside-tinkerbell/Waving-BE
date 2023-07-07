package com.bsideTinkerbell.wavingBe.controller;

import com.bsideTinkerbell.wavingBe.repository.FriendProfileRepository;
import com.bsideTinkerbell.wavingBe.repository.FriendRepository;
import com.bsideTinkerbell.wavingBe.security.JwtAuthenticationFilter;
import com.bsideTinkerbell.wavingBe.security.JwtService;
import com.bsideTinkerbell.wavingBe.service.FriendService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

//@Disabled
@WebMvcTest({
				FriendController.class
})
public abstract class ControllerTest {

		// REST DOCS 관련
		@Autowired
		protected ObjectMapper objectMapper;
		@Autowired
		protected MockMvc mockMvc;

		// 지인 관련
		@MockBean
		protected FriendService friendService;
		@MockBean
		protected FriendRepository friendRepository;
		@MockBean
		protected FriendProfileRepository profileRepository;

		// JWT 관련
		@MockBean
		protected JwtAuthenticationFilter jwtAuthenticationFilter;
		@MockBean
		protected JwtService jwtservice;


		public String createJson(Object dto) throws JsonProcessingException{
				return objectMapper.writeValueAsString(dto);
		}

}
