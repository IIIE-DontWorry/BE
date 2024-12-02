package com.iiie.server.service;

import com.iiie.server.domain.Guardian;
import com.iiie.server.domain.GuardianRequest;
import com.iiie.server.dto.GuardianRequestDTO.GuardianRequestCreationRequest;
import com.iiie.server.exception.NotFoundException;
import com.iiie.server.repository.GuardianRepository;
import com.iiie.server.repository.GuardianRequestRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GuardianRequestService {

  private final GuardianRepository guardianRepository;
  private final GuardianRequestRepository guardianRequestRepository;

  public GuardianRequestService(
      GuardianRepository guardianRepository, GuardianRequestRepository guardianRequestRepository) {
    this.guardianRepository = guardianRepository;
    this.guardianRequestRepository = guardianRequestRepository;
  }

  @Transactional
  public void createGuardianRequest(Long guardianId, GuardianRequestCreationRequest request) {
    Guardian guardian =
        guardianRepository
            .findById(guardianId)
            .orElseThrow(() -> new NotFoundException("guardian", guardianId, "존재하지 않는 보호자 ID입니다."));

    // TODO : 나머지 요청사항은 old가 되도록
    Optional<List<GuardianRequest>> optionalGuardianRequest =
        guardianRequestRepository.findAllByGuardianId(guardianId);
    if (optionalGuardianRequest.isPresent()) {
      List<GuardianRequest> guardianRequests = optionalGuardianRequest.get();
      guardianRequests.forEach(GuardianRequest::changeToOld);
    }

    List<GuardianRequest> guardianRequests =
        request.getRequests().stream()
            .map(
                r -> {
                  GuardianRequest guardianRequest =
                      GuardianRequest.builder().request(r).isCheck(false).build();
                  guardianRequest.setGuardian(guardian);
                  return guardianRequest; // 반환 필요
                })
            .toList();
    guardianRequestRepository.saveAll(guardianRequests);
    // TODO(THINK ABOUT) :  현재 간병보고서와 연관관계 매핑 안함. init메서드 보완하면서 살펴보자.(근데 이 시점에 매핑 안되는게 맞긴해.)
  }
}
