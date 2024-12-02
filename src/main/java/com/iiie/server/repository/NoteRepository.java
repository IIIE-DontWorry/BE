package com.iiie.server.repository;

import com.iiie.server.domain.Note;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
  // 간병인 ID로 쪽지 조회
  List<Note> findAllByCaregiverId(Long caregiverId);

  // 보호자 ID로 쪽지 조회
  List<Note> findAllByGuardianId(Long guardianId);

  // 간병인 ID로 쪽지 조회 (최신 3개)
  List<Note> findTop3ByCaregiverIdOrderByCreatedAtDesc(Long caregiverId);

  // 보호자 ID로 쪽지 조회 (최신 3개)
  List<Note> findTop3ByGuardianIdOrderByCreatedAtDesc(Long guardianId);
}
