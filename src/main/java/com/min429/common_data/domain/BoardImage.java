package com.min429.common_data.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.min429.common_data.util.dto.RequestBoardImageResult;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document("admin-board-image")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardImage {

	@Id
	private String id;

	private String boardId;
	private Long contentId;
	private String imageName;
	private List<String> originalImageUrls;
	private String serialNum;
	private List<String> smallImageUrls;
	private String cpyrhtDivCd;

	static public void from(Map<Long, BoardImage> boardImageMap, RequestBoardImageResult result) {
		BoardImage boardImage = boardImageMap.get(result.contentId());
		if (boardImage == null) {
			boardImageMap.put(result.contentId(),
				BoardImage.builder()
					.contentId(result.contentId())
					.imageName(result.imageName())
					.originalImageUrls(new ArrayList<>())
					.serialNum(result.serialNum())
					.smallImageUrls(new ArrayList<>())
					.cpyrhtDivCd(result.cpyrhtDivCd())
					.build()
			);
		}
		boardImageMap.get(result.contentId())
			.originalImageUrls.add(result.originalImageUrl());
		boardImageMap.get(result.contentId())
			.smallImageUrls.add(result.smallImageUrl());
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}
}
