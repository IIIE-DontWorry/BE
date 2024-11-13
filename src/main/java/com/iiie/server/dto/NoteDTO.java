package com.iiie.server.dto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
public class NoteDTO {
    private Long id;
    private String createdBy;
    private LocalDateTime createdAt;
    private String noteContent;
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
    public static class NoteRequest {
        private String noteContent;
    }
}