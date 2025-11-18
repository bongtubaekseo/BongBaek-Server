package org.appjam.bongbaek.domain.content.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.appjam.bongbaek.domain.content.dto.request.ContentWriteDto;
import org.appjam.bongbaek.domain.content.dto.response.ContentDetailResponseDto;
import org.appjam.bongbaek.domain.content.dto.response.ContentHomeResponseDto;
import org.appjam.bongbaek.domain.content.dto.response.ContentListDto;
import org.appjam.bongbaek.global.api.response.SuccessResponse;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "경조사 컨텐츠 정보", description = "경조사 컨텐츠 정보 관련 API")
public interface ContentController {
    @Operation(summary = "경조사 컨텐츠 정보 상세 조회", description = "경조사 컨텐츠 정보를 상세 조회합니다.")
    SuccessResponse<ContentDetailResponseDto> getContentDetail(String contentId);

    @Operation(summary = "경조사 컨텐츠 정보 홈 조회", description = "홈 화면에서 경조사 컨텐츠 정보 3개를 최신순으로 조회합니다.")
    SuccessResponse<ContentHomeResponseDto> getContentForHome();

    @Operation(summary = "경조사 컨텐츠 정보 더보기 탭 조회", description = "더보기 탭에서 경조사 컨텐츠 정보들을 최신순, 카테고리 별로 조회합니다.")
    SuccessResponse<ContentListDto> getContentList(int page, String category);

    @Operation(summary = "경조사 컨텐츠 정보 생성", description = "경조사 컨텐츠 정보를 생성합니다.")
    SuccessResponse<Void> createContent(ContentWriteDto request, MultipartFile thumbnailFile);

    @Operation(summary = "경조사 컨텐츠 썸네일 변경", description = "경조사 컨텐츠 썸네일을 변경합니다.")
    SuccessResponse<Void> updateThumbnail(String contentId, MultipartFile newThumbnailFile);

    @Operation(summary = "경조사 컨텐츠 메인 이미지 업로드", description = "경조사 컨텐츠 메인 이미지를 업로드합니다.")
    SuccessResponse<Void> uploadMainImage(String contentId, MultipartFile mainImageFile);

    @Operation(summary = "경조사 컨텐츠 정보 삭제", description = "경조사 컨텐츠 정보를 삭제합니다.")
    SuccessResponse<Void> deleteContent( String contentId);
}
