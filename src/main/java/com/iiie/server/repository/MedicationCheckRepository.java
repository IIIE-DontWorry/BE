package com.iiie.server.repository;

import com.iiie.server.domain.MedicationCheck;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationCheckRepository extends JpaRepository<MedicationCheck, Long> {

  Optional<List<MedicationCheck>> findAllByPatientId(Long patientId);
}
