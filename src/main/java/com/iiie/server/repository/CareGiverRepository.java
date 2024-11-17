package com.iiie.server.repository;

import com.iiie.server.domain.Caregiver;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareGiverRepository extends JpaRepository<Caregiver, Long> {

  Optional<Caregiver> findByKakaoId(Long kakaoId);
}
