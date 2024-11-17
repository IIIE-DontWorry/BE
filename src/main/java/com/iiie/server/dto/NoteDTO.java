package com.iiie.server.dto;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
public class NoteDTO {
    private Long id;
    private String createdBy;
    private LocalDate createdAt;
    private String noteContent;

    @Getter
    @Setter
    public static class NoteRequest {
        private Long guardianId; //샌더
        private Long careGiverId;  //리시버
        private String noteContent;
    }

    @Getter
    @Setter
    public static class NoteResponse {
        private Long id;
        private String createdBy;
        private LocalDate createdAt;
        private String noteContent;
    }
}