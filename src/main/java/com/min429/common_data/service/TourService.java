package com.min429.common_data.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.min429.common_data.controller.dto.SpotResponse;
import com.min429.common_data.domain.enums.Area;
import com.min429.common_data.domain.Spot;
import com.min429.common_data.exception.DataException;
import com.min429.common_data.repository.mongo.SpotRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class TourService {

	private final SpotRepository spotRepository;

	public Page<SpotResponse> findAllByArea(Area area, int page, int size) {
		Pageable pageable = PageRequest.of(page - 1, size);
		Page<Spot> spots = spotRepository.findAllByArea(area, pageable);

		return spots.map(spot -> SpotResponse.builder()
			.title(spot.getTitle())
			.firstimage(spot.getFirstimage())
			.addr1(spot.getAddr1())
			.overview(spot.getOverview())
			.build());
	}
}
