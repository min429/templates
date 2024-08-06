package com.min429.common_data.domain.enums;

public enum Sigungu {
	Unknown(0L),
	남제주군(1L),
	북제주군(2L),
	서귀포시(3L),
	제주시(4L);

	private final Long code;

	Sigungu(Long code) {
		this.code = code;
	}

	public Long getCode() {
		return code;
	}

	public static Sigungu fromCode(Long code) {
		for (Sigungu sigungu : Sigungu.values()) {
			if (sigungu.getCode().equals(code)) {
				return sigungu;
			}
		}
		throw new IllegalArgumentException("Unknown code: " + code);
	}
}
