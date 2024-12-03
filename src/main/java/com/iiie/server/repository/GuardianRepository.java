package com.iiie.server.repository;

import com.iiie.server.domain.Guardian;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuardianRepository extends JpaRepository<Guardian, Long> {

  Optional<Guardian> findByUniqueCode(UUID guardianUniqueCode);

  Optional<Guardian> findByKakaoId(Long kakaoId);
}
