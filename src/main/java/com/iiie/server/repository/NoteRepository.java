package com.iiie.server.repository;

import com.iiie.server.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    // 간병인 ID로 쪽지 조회
    List<Note> findAllByCaregiverId(Long caregiverId);

    // 보호자 ID로 쪽지 조회
    List<Note> findAllByGuardianId(Long guardianId);

    // 가장 최근에 작성한 쪽지 3개 조회
    List<Note> findTop3ByOrderByIdDesc();
}