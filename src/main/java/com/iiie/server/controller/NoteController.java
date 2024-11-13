package com.iiie.server.controller;
import com.iiie.server.domain.Note;
import com.iiie.server.service.NoteService;
import com.iiie.server.utils.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;
    public NoteController(NoteService noteService) { this.noteService = noteService; }
    @PostMapping("/{careGiverId}/{guardianId}/add")
    @Operation(summary = "쪽지 추가", description = "쪽지 내용, 작성자, 날짜를 저장합니다.")
    public SuccessResponse<Void> addNote(@PathVariable Long careGiverId, @PathVariable Long guardianId) {
        Note note = noteService.addNote(careGiverId, guardianId);
        return new SuccessResponse<>("쪽지 추가 완료", null);
    }
    @DeleteMapping("/{noteId}/delete")
    @Operation(summary = "쪽지 삭제", description = "선택한 쪽지를 삭제합니다.")
    public SuccessResponse<Void> deleteNote(@PathVariable Long noteId) {
        noteService.deleteNote(noteId);
        return new SuccessResponse<>("쪽지 삭제 완료", null);
    }
    @PostMapping("/{noteId}/copy")
    @Operation(summary = "쪽지 복사", description = "선택한 쪽지를 복사합니다.")
    public SuccessResponse<Void> copyNote(@PathVariable Long noteId) {
        Note note = noteService.copyNote(noteId);
        return new SuccessResponse<>("쪽지 복사 완료.", null);
    }
    @GetMapping("/{careGiverId}")
    @Operation(summary = "쪽지 조회", description = "작성된 쪽지 목록을 모두 조회.")
    public SuccessResponse<Void> getNotes(@PathVariable Long careGiverId, @PathVariable Long guardianId) {
        Note notes = noteService.getNotes(careGiverId, guardianId);
        return new SuccessResponse<>("쪽지 목록 조회 완료", null);
    }
}