package com.iiie.server.controller;
import com.iiie.server.dto.NoteDTO;
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
    public SuccessResponse<List<GalleryDTO.GetImageResponse>> getImages(@RequestBody GalleryDTO.GetImageRequest getImageRequest) {
        List<GalleryDTO.GetImageResponse> galleries = galleryService.getImages(getImageRequest);
        return new SuccessResponse<>("이미지 조회 완료", galleries);
    }

    @PostMapping("/upload")
    @Operation(summary = "이미지 업로드", description = "선택한 이미지들을 업로드합니다.")
    public SuccessResponse<Void> uploadImages(@ModelAttribute GalleryDTO.UploadImages uploadImages) {
        galleryService.uploadImages(uploadImages);
        return new SuccessResponse<>("이미지 업로드 완료", null);
    }
}
