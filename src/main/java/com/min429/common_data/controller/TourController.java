package com.min429.common_data.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.min429.common_data.config.SwaggerResponseEntity;
import com.min429.common_data.controller.dto.SpotResponse;
import com.min429.common_data.service.TourService;
import com.min429.common_data.util.TourApiUtil;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tour/data")
public class TourController {

	private final TourService tourService;
	private final TourApiUtil tourApiUtil;

	@GetMapping
	public SwaggerResponseEntity<List<SpotResponse>> findAllByAreacode(
		@Parameter(name = "area", description = "지역 이름", in = ParameterIn.QUERY) Long areacode) {
		return SwaggerResponseEntity.success(tourService.findAllByAreacode(areacode));
	}
}
