package com.iiie.server.controller;
import com.iiie.server.service.GalleryService;
import com.iiie.server.dto.GalleryDTO;
import com.iiie.server.utils.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/gallery")
public class GalleryController {

    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) { this.galleryService = galleryService; }

    @PostMapping("")
    @Operation(summary = "갤러리 조회", description = "전체 사용자들은 환자의 갤러리를 조회합니다.")
    public SuccessResponse<List<GalleryDTO.GetGalleryResponse>> getImages(@RequestBody GalleryDTO.GetGalleryRequest getGalleryRequest) {
        List<GalleryDTO.GetGalleryResponse> galleries = galleryService.getImages(getGalleryRequest);
        return new SuccessResponse<>("이미지 조회 완료", galleries);
    }

    @PostMapping("/recent")
    @Operation(summary = "최근 갤러리 조회", description = "전체 사용자들은 환자의 최근 갤러리 3개를 조회합니다.")
    public SuccessResponse<List<GalleryDTO.GetGalleryResponse>> getRecentGalleries(@RequestBody GalleryDTO.GetGalleryRequest getGalleryRequest) {
        List<GalleryDTO.GetGalleryResponse> galleries = galleryService.getRecentGalleries(getGalleryRequest);
        return new SuccessResponse<>("최근 갤러리 조회 완료", galleries);
    }

    @PostMapping("/upload")
    @Operation(summary = "이미지 업로드", description = "선택한 이미지들을 업로드합니다.")
    public SuccessResponse<Void> uploadImages(@ModelAttribute GalleryDTO.UploadGallery uploadGallery) {
        galleryService.uploadImages(uploadGallery);
        return new SuccessResponse<>("이미지 업로드 완료", null);
    }

    @DeleteMapping("/delete/{galleryId}")
    @Operation(summary = "갤러리 삭제", description = "선택한 갤러리를 삭제합니다.")
    public SuccessResponse<Void> deleteGallery(@PathVariable Long galleryId) {
        galleryService.deleteGallery(galleryId);
        return new SuccessResponse<>("갤러리 삭제 완료", null);
    }

    @PatchMapping("")
    @Operation(summary = "갤러리 수정", description = "갤러리 제목 수정, 이미지 추가 및 삭제를 처리합니다.")
    public SuccessResponse<Void> updateGallery(@ModelAttribute GalleryDTO.UpdateGalleryRequest request) {
        galleryService.updateGallery(request);
        return new SuccessResponse<>("갤러리 수정 완료", null);
    }
}
