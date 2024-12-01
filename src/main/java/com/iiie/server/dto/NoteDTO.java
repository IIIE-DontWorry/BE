package com.iiie.server.dto;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

public class NoteDTO {
    private Long id;
    private String createdBy;
    private LocalDateTime createdAt;
    private String noteContent;

    @Getter
    @Setter
    public static class InquiryRequest {
        private Long guardianId;
        private Long caregiverId;
    }

    @Getter
    @Setter
    public static class AddRequest {
        private Long guardianId;
        private Long caregiverId;
        private String noteContent;
    }

    @Getter
    @Setter
    public static class NoteResponse {
        private Long id;
        private String createdBy;
        private LocalDateTime createdAt;
        private String noteContent;
    }

    @Getter
    @Setter
    public static class DeleteNote {
        private Long id;
    }
}