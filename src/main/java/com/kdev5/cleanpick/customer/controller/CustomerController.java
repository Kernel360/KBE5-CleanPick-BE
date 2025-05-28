package com.kdev5.cleanpick.customer.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdev5.cleanpick.customer.service.CustomerService;
import com.kdev5.cleanpick.user.service.dto.request.SignUpRequestDto;
import com.kdev5.cleanpick.customer.service.dto.response.CustomerResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CustomerController {

	private final CustomerService customerService;


}
