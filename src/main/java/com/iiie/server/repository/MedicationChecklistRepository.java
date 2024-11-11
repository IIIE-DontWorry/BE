package com.iiie.server.repository;

import com.iiie.server.domain.MedicationCheckList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationChecklistRepository extends JpaRepository<MedicationCheckList, Long> {

  Optional<List<MedicationCheckList>> findAllByPatientId(Long patientId);
}
