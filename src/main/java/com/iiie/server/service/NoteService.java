package com.iiie.server.service;
import com.iiie.server.domain.Note;
import com.iiie.server.domain.Caregiver;
import com.iiie.server.domain.Guardian;
import com.iiie.server.repository.NoteRepository;
import com.iiie.server.repository.CaregiverRepository;
import com.iiie.server.repository.GuardianRepository;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional(readOnly = true)
public class NoteService {
    private final NoteRepository noteRepository;
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }
    @Transactional
    public Note addNote(Long caregiverId, Long guardianId, String noteContent) {
        Note note = Note.builder()
                .caregiver(caregiver)
                .guardian(guardian)
                .noteContent(noteContent)
                .build();
        return noteRepository.save(note);
    }
    @Transactional
    public void deleteNote(Long noteId) {
        noteRepository.deleteById(noteId);
    }
    @Transactional
    public Note copyNote(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new Exception("Note not found with id: " + noteId));
        String noteContent = note.getNoteContent();
        StringSelection stringSelection = new StringSelection(noteContent);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        return note;
    }
    public Note getNotes(Long caregiverId, Long guardianId) {
    }
}