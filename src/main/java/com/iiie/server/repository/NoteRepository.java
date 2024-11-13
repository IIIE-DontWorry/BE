package com.iiie.server.repository;
import com.iiie.server.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
public interface NoteRepository extends JpaRepository<Note, Long> {}