package com.iiie.server.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class GalleryDTO {
  private Long id;
  private LocalDateTime createdAt;
  private String createdBy;
  private String title;
  private List<String> images;

  @Getter
  @Setter
  @Builder
  public static class GetGalleryResponse {
    private Long galleryId;
    private String createdBy;
    private LocalDateTime createdAt;
    private String title;
    private List<ImageInfo> images;
  }

    @Getter
    @Setter
    public static class UploadGallery {
        private String createdBy;
        private List<String> images;
        private String title;
    }

    @Getter
    @Setter
    public static class UpdateGalleryRequest {
        private Long galleryId;
        private String title;
        private List<String> addImages;
        private List<Long> deleteImageIds;
    }

    @Getter
    @Setter
    @Builder
    public static class ImageInfo {
        private Long imageId;
        private String imageUrl;
    }
}
