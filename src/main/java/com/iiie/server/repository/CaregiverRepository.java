package com.iiie.server.repository;

import com.iiie.server.domain.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaregiverRepository extends JpaRepository <Caregiver, Long> { }
