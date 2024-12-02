package com.iiie.server.repository;

import com.iiie.server.domain.GuardianRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuardianRequestRepository extends JpaRepository<GuardianRequest, Long> {

  Optional<List<GuardianRequest>> findAllByGuardianId(Long guardianId);

  List<GuardianRequest> findAllByIsNewTrueAndGuardianId(Long id);
}
