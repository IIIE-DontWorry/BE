package com.iiie.server.dto;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
public class GalleryDTO {
    private Long id;
    private String createdBy;
    private LocalDate createdAt;
    private String title;
}
