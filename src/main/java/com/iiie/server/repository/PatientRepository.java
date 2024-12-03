package com.iiie.server.repository;

import com.iiie.server.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {}
