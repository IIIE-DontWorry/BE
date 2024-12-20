package com.iiie.server.service;

import com.iiie.server.domain.Caregiver;
import com.iiie.server.domain.Guardian;
import com.iiie.server.domain.Note;
import com.iiie.server.dto.NoteDTO;
import com.iiie.server.exception.NotFoundException;
import com.iiie.server.repository.CaregiverRepository;
import com.iiie.server.repository.GuardianRepository;
import com.iiie.server.repository.NoteRepository;
import com.iiie.server.utils.NoteEvaluation;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class NoteService {

  private final NoteRepository noteRepository;
  private final CaregiverRepository caregiverRepository;
  private final GuardianRepository guardianRepository;
  private final NoteEvaluation noteEvaluation;

  public NoteService(
      NoteRepository noteRepository,
      CaregiverRepository caregiverRepository,
      GuardianRepository guardianRepository,
      NoteEvaluation noteEvaluation) {
    this.noteRepository = noteRepository;
    this.caregiverRepository = caregiverRepository;
    this.guardianRepository = guardianRepository;
    this.noteEvaluation = noteEvaluation;
  }

  @Transactional(readOnly = true)
  public List<NoteDTO.NoteResponse> inquiryNotes(NoteDTO.InquiryRequest inquiryRequest) {
    List<Note> notes;

    // 간병인 ID로 필터링
    if (inquiryRequest.getCaregiverId() != null) {
      notes = noteRepository.findAllByCaregiverId(inquiryRequest.getCaregiverId());
    }
    // 보호자 ID로 필터링
    else if (inquiryRequest.getGuardianId() != null) {
      notes = noteRepository.findAllByGuardianId(inquiryRequest.getGuardianId());
    } else {
      throw new IllegalArgumentException("간병인 ID 또는 보호자 ID 중 하나는 반드시 제공되어야 합니다.");
    }

    // Note 엔티티를 NoteDTO.NoteResponse로 변환
    return notes.stream().map(this::convertToNoteResponse).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<NoteDTO.NoteResponse> getLatestNotes(NoteDTO.InquiryRequest inquiryRequest) {
    List<Note> notes;

    // 간병인 ID로 조회
    if (inquiryRequest.getCaregiverId() != null) {
      notes =
          noteRepository.findTop3ByCaregiverIdOrderByCreatedAtDesc(inquiryRequest.getCaregiverId());
    }
    // 보호자 ID로 조회
    else if (inquiryRequest.getGuardianId() != null) {
      notes =
          noteRepository.findTop3ByGuardianIdOrderByCreatedAtDesc(inquiryRequest.getGuardianId());
    } else {
      throw new IllegalArgumentException("간병인 ID 또는 보호자 ID 중 하나는 반드시 제공되어야 합니다.");
    }

    return notes.stream().map(this::convertToNoteResponse).collect(Collectors.toList());
  }

  @Transactional
  public NoteDTO.NoteResponse addNote(NoteDTO.AddRequest addRequest) {
    Caregiver caregiver = null;
    Guardian guardian = null;
    String createdByType = null;

    // 간병인이 작성한 경우
    if (addRequest.getCaregiverId() != null) {
      caregiver =
          caregiverRepository
              .findById(addRequest.getCaregiverId())
              .orElseThrow(
                  () ->
                      new NotFoundException(
                          "caregiver", addRequest.getCaregiverId(), "존재하지 않는 간병인입니다."));
      guardian = caregiver.getGuardian();
      if (guardian == null) {
        throw new IllegalStateException("매칭된 보호자를 찾을 수 없습니다.");
      }
      createdByType = "caregiver";

      // Note 엔티티 생성
      Note note =
          Note.builder()
              .caregiver(caregiver)
              .guardian(caregiver.getGuardian())
              .createdBy(createdByType)
              .noteContent(addRequest.getNoteContent())
              .build();

      Note savedNote = noteRepository.save(note);

      // gpt 평가 요청
      String evaluation = noteEvaluation.evaluateContent(addRequest.getNoteContent());

      switch (evaluation) {
        case "0": // 부정적인 대화
          caregiver.updateMannerScore(-1.0);
          break;
        case "1": // 평범한 대화
          break;
        case "2": // 긍정적인 대화
          caregiver.updateMannerScore(1.0);
          break;
        default:
          throw new IllegalArgumentException("Unknown evaluation value: " + evaluation);
      }
      return convertToNoteResponseGPT(savedNote, evaluation);
    }

    // 보호자가 작성한 경우
    else if (addRequest.getGuardianId() != null) {
      guardian =
          guardianRepository
              .findById(addRequest.getGuardianId())
              .orElseThrow(
                  () ->
                      new NotFoundException(
                          "guardian", addRequest.getGuardianId(), "존재하지 않는 보호자입니다."));
      caregiver = guardian.getCaregiver();
      if (caregiver == null) {
        throw new IllegalStateException("매칭된 간병인을 찾을 수 없습니다.");
      }
      createdByType = "guardian";

      // Note 엔티티 생성
      Note note =
          Note.builder()
              .caregiver(guardian.getCaregiver())
              .guardian(guardian)
              .createdBy(createdByType)
              .noteContent(addRequest.getNoteContent())
              .build();

      Note savedNote = noteRepository.save(note);

      // gpt 평가 요청
      String evaluation = noteEvaluation.evaluateContent(addRequest.getNoteContent());

      switch (evaluation) {
        case "0": // 부정적인 대화
          guardian.updateMannerScore(-1.0);
          break;
        case "1": // 평범한 대화
          break;
        case "2": // 긍정적인 대화
          guardian.updateMannerScore(1.0);
          break;
        default:
          throw new IllegalArgumentException("Unknown evaluation value: " + evaluation);
      }

      return convertToNoteResponseGPT(savedNote, evaluation);
    } else {
      throw new IllegalArgumentException("간병인 ID 또는 보호자 ID 중 하나는 반드시 제공되어야 합니다.");
    }
  }

  private NoteDTO.NoteResponse convertToNoteResponseGPT(Note note, String evaluation) {
    NoteDTO.NoteResponse response = new NoteDTO.NoteResponse();
    response.setId(note.getId());
    response.setCreatedBy(note.getCreatedBy());
    response.setCreatedAt(note.getCreatedAt());
    response.setNoteContent(note.getNoteContent());
    response.setGptResponse(evaluation);
    return response;
  }

  private NoteDTO.NoteResponse convertToNoteResponse(Note note) {
    NoteDTO.NoteResponse response = new NoteDTO.NoteResponse();
    response.setId(note.getId());
    response.setCreatedBy(note.getCreatedBy());
    response.setCreatedAt(note.getCreatedAt());
    response.setNoteContent(note.getNoteContent());
    return response;
  }

  // 쪽지 삭제
  @Transactional
  public void deleteNote(Long noteId) {
    Note note =
        noteRepository
            .findById(noteId)
            .orElseThrow(() -> new NotFoundException("note", noteId, "존재하지 않는 쪽지입니다."));
    noteRepository.delete(note);
  }

  // 보호자 매너 점수 조회
  @Transactional(readOnly = true)
  public NoteDTO.MannerScore getGuardianScore(Long guardianId) {
    Guardian guardian =
        guardianRepository
            .findById(guardianId)
            .orElseThrow(() -> new NotFoundException("guardian", guardianId, "존재하지 않는 보호자 입니다."));
    NoteDTO.MannerScore mannerScore = new NoteDTO.MannerScore();
    mannerScore.setScore(guardian.getMannerScore());

    return mannerScore;
  }

  // 간병인 매너 점수 조회
  @Transactional(readOnly = true)
  public NoteDTO.MannerScore getCaregiverScore(Long caregiverId) {
    Caregiver caregiver =
        caregiverRepository
            .findById(caregiverId)
            .orElseThrow(() -> new NotFoundException("caregiver", caregiverId, "존재하지 않는 간병인 입니다."));
    NoteDTO.MannerScore mannerScore = new NoteDTO.MannerScore();
    mannerScore.setScore(caregiver.getMannerScore());

    return mannerScore;
  }
}
