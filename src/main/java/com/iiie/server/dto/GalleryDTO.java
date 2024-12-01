package com.iiie.server.dto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

public class GalleryDTO {
    private Long id;
    private LocalDateTime createdAt;
    private Long caregiverId;
    private Long guardianId;
    private Long createdBy;
    private String title;

    @Getter
    @Setter
    public static class GetGalleryRequest {
        private Long caregiverId;
        private Long guardianId;
    }

    @Getter
    @Setter
    @Builder
    public static class GetGalleryResponse {
        private Long galleryId;
        private Long createdBy;
        private LocalDateTime createdAt;
        private List<ImageInfo> images;
        private String title;
    }

    @Getter
    @Setter
    @Builder
    public static class ImageInfo {
        private Long imageId;
        private String imageUrl;
    }

    @Getter
    @Setter
    public static class UploadGallery {
        private Long caregiverId;
        private Long guardianId;
        private List<MultipartFile> images;
        private String title;
    }

    @Getter
    @Setter
    public static class UpdateGalleryRequest {
        private Long galleryId;
        private String title;
        private List<MultipartFile> addImages;
        private List<Long> deleteImageIds;
    }
}
