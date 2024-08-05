package com.min429.common_data.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.min429.common_data.config.SwaggerResponseEntity;
import com.min429.common_data.controller.dto.SpotResponse;
import com.min429.common_data.domain.enums.Area;
import com.min429.common_data.service.TourService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tour/")
public class TourController {

	private final TourService tourService;

	@GetMapping("/spots")
	public SwaggerResponseEntity<Page<SpotResponse>> findAll(
		@RequestParam(name = "area") Area area,
		@RequestParam(name = "page", defaultValue = "1") int page,
		@RequestParam(name = "size", defaultValue = "10") int size) {

		Page<SpotResponse> spotResponses = tourService.findAllByArea(area, page, size);
		return SwaggerResponseEntity.success(spotResponses);
	}
}
