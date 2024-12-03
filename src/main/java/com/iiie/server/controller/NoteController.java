package com.iiie.server.controller;

import com.iiie.server.dto.NoteDTO;
import com.iiie.server.service.NoteService;
import com.iiie.server.utils.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notes")
public class NoteController {

  private final NoteService noteService;

  public NoteController(NoteService noteService) {
    this.noteService = noteService;
  }

  @PostMapping("")
  @Operation(summary = "쪽지 조회", description = "작성된 쪽지 목록을 모두 조회합니다.")
  public SuccessResponse<List<NoteDTO.NoteResponse>> inquiryNotes(
      @RequestBody NoteDTO.InquiryRequest inquiryRequest) {
    List<NoteDTO.NoteResponse> messages = noteService.inquiryNotes(inquiryRequest);
    return new SuccessResponse<>("쪽지 목록 조회 완료", messages);
  }

  @PostMapping("/latest")
  @Operation(summary = "최근 쪽지 조회", description = "가장 최근에 작성된 쪽지 3개를 조회합니다.")
  public SuccessResponse<List<NoteDTO.NoteResponse>> getLatestNotes(
      @RequestBody NoteDTO.InquiryRequest inquiryRequest) {
    List<NoteDTO.NoteResponse> notes = noteService.getLatestNotes(inquiryRequest);
    return new SuccessResponse<>("최근 쪽지 조회 완료", notes);
  }

  @PostMapping("/add")
  @Operation(summary = "쪽지 추가", description = "쪽지 내용, 작성자, 날짜를 저장합니다.")
  public SuccessResponse<NoteDTO.NoteResponse> addNote(@RequestBody NoteDTO.AddRequest addRequest) {
    NoteDTO.NoteResponse addNote = noteService.addNote(addRequest);
    return new SuccessResponse<>("쪽지 추가 완료", addNote);
  }

  @DeleteMapping("/delete")
  @Operation(summary = "쪽지 삭제", description = "선택한 쪽지를 삭제합니다.")
  public SuccessResponse<Void> deleteNote(@RequestBody NoteDTO.DeleteNote deleteNote) {
    noteService.deleteNote(deleteNote.getId());
    return new SuccessResponse<>("쪽지 삭제 완료", null);
  }

  @GetMapping("/score/guardian/{guardianId}")
  @Operation(summary = "보호자 매너 점수 조회", description = "보호자 점수를 조회합니다.")
  public SuccessResponse<NoteDTO.MannerScore> getGuaridanScore(
      @PathVariable(name = "guardianId") Long guardianId) {
    NoteDTO.MannerScore mannerScore = noteService.getGuardianScore(guardianId);
    return new SuccessResponse<>("보호자 매너 점수 조회 완료", mannerScore);
  }

  @GetMapping("/score/caregiver/{caregiverId}")
  @Operation(summary = "간병인 매너 점수 조회", description = "간병인 점수를 조회합니다.")
  public SuccessResponse<NoteDTO.MannerScore> getCaregiverScore(
      @PathVariable(name = "caregiverId") Long caregiverId) {
    NoteDTO.MannerScore mannerScore = noteService.getCaregiverScore(caregiverId);
    return new SuccessResponse<>("간병인 매너 점수 조회 완료", mannerScore);
  }
}
