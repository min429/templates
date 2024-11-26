package com.min429.common_data.util.dto;

import lombok.Builder;

@Builder
public record RequestBoardImageResult(
	Long contentId,
	String imageName,
	String originalImageUrl,
	String serialNum,
	String smallImageUrl,
	String cpyrhtDivCd
) {
}
