package com.min429.common_data.domain;

import org.springframework.data.annotation.Id;

import com.min429.common_data.domain.enums.Area;
import com.min429.common_data.domain.enums.ContentType;
import com.min429.common_data.domain.enums.Sigungu;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Info {

	@Id
	private String id;

	private String addr1; // 주소
	private Area area; // 지역코드
	private String cat1; // 대분류 코드
	private String cat2; // 중분류 코드
	private String cat3; // 소분류 코드
	private Long contentid; // 콘텐츠 ID
	private ContentType contentType; // 콘텐츠 타입 ID
	private String firstimage; // 대표이미지
	private Double mapx; // GPS X좌표
	private Double mapy; // GPS Y좌표
	private Sigungu sigungu; // 시군구코드
	private String title; // 콘텐츠제목
	private String tel; // 전화번호
	private String overview; // 소개
}
