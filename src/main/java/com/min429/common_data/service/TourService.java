package com.min429.common_data.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.min429.common_data.controller.dto.SpotResponse;
import com.min429.common_data.domain.Spot;
import com.min429.common_data.exception.DataException;
import com.min429.common_data.repository.mongo.SpotRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class TourService {

	private final SpotRepository spotRepository;

	public List<SpotResponse> findAllByAreacode(Long areacode) {
		List<Spot> spots = spotRepository.findAllByAreacode(areacode)
			.orElseThrow(() -> new DataException("데이터 없음"));

		List<SpotResponse> spotResponse = new LinkedList<>();
		for (Spot spot : spots) {
			spotResponse.add(SpotResponse.builder()
				.title(spot.getTitle())
				.firstimage(spot.getFirstimage())
				.addr1(spot.getAddr1())
				.overview(spot.getOverview()).build());
		}
		return spotResponse;
	}
}
