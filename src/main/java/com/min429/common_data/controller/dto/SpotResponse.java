package com.min429.common_data.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SpotResponse(
	String title, // 콘텐츠명 (제목)
	String firstimage, // 대표 이미지 (원본) URL
	String addr1, // 주소
	String overview // 소개
) {
}
