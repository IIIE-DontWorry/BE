package com.iiie.server.service;
import com.iiie.server.domain.Note;
import com.iiie.server.domain.Caregiver;
import com.iiie.server.domain.Guardian;
import com.iiie.server.dto.NoteDTO;
import com.iiie.server.exception.NotFoundException;
import com.iiie.server.repository.NoteRepository;
import com.iiie.server.repository.CaregiverRepository;
import com.iiie.server.repository.GuardianRepository;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class NoteService {

    private final NoteRepository noteRepository;
    private final CaregiverRepository caregiverRepository;
    private final GuardianRepository guardianRepository;

    public NoteService(NoteRepository noteRepository,
                       CaregiverRepository caregiverRepository,
                       GuardianRepository guardianRepository) {
        this.noteRepository = noteRepository;
        this.caregiverRepository = caregiverRepository;
        this.guardianRepository = guardianRepository;
    }

    @Transactional(readOnly = true)
    public List<NoteDTO.NoteResponse> getNotes(NoteDTO.NoteRequest noteRequest) {
        List<Note> notes;

        // 간병인 ID로 필터링
        if (noteRequest.getCareGiverId() != null) {
            notes = noteRepository.findAllByCaregiverId(noteRequest.getCareGiverId());
        }
        // 보호자 ID로 필터링
        else if (noteRequest.getGuardianId() != null) {
            notes = noteRepository.findAllByGuardianId(noteRequest.getGuardianId());
        } else {
            throw new IllegalArgumentException("간병인 ID 또는 보호자 ID 중 하나는 반드시 제공되어야 합니다.");
        }

        // Note 엔티티를 NoteDTO.NoteResponse로 변환
        return notes.stream()
                .map(this::convertToNoteResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public NoteDTO.NoteResponse addNote(NoteDTO.NoteRequest noteRequest) {
        Caregiver caregiver = null;
        Guardian guardian = null;
        String createdByType = null;

        // 간병인이 작성한 경우
        if (noteRequest.getCareGiverId() != null) {
            caregiver = caregiverRepository.findById(noteRequest.getCareGiverId())
                    .orElseThrow(() -> new NotFoundException("caregiver", noteRequest.getCareGiverId(), "존재하지 않는 간병인입니다."));
            guardian = caregiver.getGuardian();
            if (guardian == null) {
                throw new IllegalStateException("매칭된 보호자를 찾을 수 없습니다.");
            }
            createdByType = "caregiver";
        }

        // 보호자가 작성한 경우
        else if (noteRequest.getGuardianId() != null) {
            guardian = guardianRepository.findById(noteRequest.getGuardianId())
                    .orElseThrow(() -> new NotFoundException("guardian", noteRequest.getGuardianId(), "존재하지 않는 보호자입니다."));
            caregiver = guardian.getCaregiver();
            if (caregiver == null) {
                throw new IllegalStateException("매칭된 간병인을 찾을 수 없습니다.");
            }
            createdByType = "guardian";
        } else {
            throw new IllegalArgumentException("간병인 ID 또는 보호자 ID 중 하나는 반드시 제공되어야 합니다.");
        }

        // Note 엔티티 생성
        Note note = Note.builder()
                .caregiver(caregiver)
                .guardian(guardian)
                .createdBy(createdByType)
                .noteContent(noteRequest.getNoteContent())
                .build();

        // 저장
        Note savedNote = noteRepository.save(note);

        // 엔티티를 DTO로 변환하여 반환
        return convertToNoteResponse(savedNote);
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
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NotFoundException("note", noteId, "존재하지 않는 쪽지입니다."));
        noteRepository.delete(note);
    }
}