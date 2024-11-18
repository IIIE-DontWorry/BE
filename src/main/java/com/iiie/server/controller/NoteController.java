package com.iiie.server.controller;
import com.iiie.server.domain.Note;
import com.iiie.server.service.NoteService;
import com.iiie.server.dto.NoteDTO;
import com.iiie.server.utils.SuccessResponse;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) { this.noteService = noteService; }

    @PostMapping("")
    @Operation(summary = "쪽지 조회", description = "작성된 쪽지 목록을 모두 조회.")
    public SuccessResponse<List<NoteDTO.NoteResponse>> inquiryNotes(@RequestBody NoteDTO.InquiryRequest inquiryRequest) {
        List<NoteDTO.NoteResponse> messages = noteService.inquiryNotes(inquiryRequest);
        return new SuccessResponse<>("success", "쪽지 목록 조회 완료", messages);
    }

    @PostMapping("/add")
    @Operation(summary = "쪽지 추가", description = "쪽지 내용, 작성자, 날짜를 저장합니다.")
    public SuccessResponse<NoteDTO.NoteResponse> addNote(@RequestBody NoteDTO.AddRequest addRequest) {
        NoteDTO.NoteResponse addNote = noteService.addNote(addRequest);
        return new SuccessResponse<>("success", "쪽지 추가 완료", addNote);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "쪽지 삭제", description = "선택한 쪽지를 삭제합니다.")
    public SuccessResponse<Void> deleteNote(@RequestBody NoteDTO.DeleteNote deleteNote) {
        noteService.deleteNote(deleteNote.getId());
        return new SuccessResponse<>("success", "쪽지 삭제 완료", null);
    }
}