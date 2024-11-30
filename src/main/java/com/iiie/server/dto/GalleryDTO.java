package com.iiie.server.dto;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class GalleryDTO {
    private Long id;
    private LocalDate createdAt;
    private Long caregiverId;
    private Long guardianId;
    private Long createdBy;
    private String title;

    @Getter
    @Setter
    public static class UploadImages{
        private String title;
        private Long caregiverId;
        private Long guardianId;
        private List<MultipartFile> images;
    }
}
