package com.iiie.server.service;

import com.iiie.server.domain.CareerHistory;
import com.iiie.server.domain.Caregiver;
import com.iiie.server.domain.Guardian;
import com.iiie.server.exception.NotFoundException;
import com.iiie.server.repository.GuardianRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CaregiverService {

    private final GuardianRepository guardianRepository;

    public CaregiverService(GuardianRepository guardianRepository) {
        this.guardianRepository = guardianRepository;
    }

    public Caregiver createCaregiver(String name, String phone, String hospital, List<String> careerHistoryDescriptions,
            UUID guardianUniqueCode) {

        Guardian guardian = guardianRepository.findByUniqueCode(guardianUniqueCode)
                .orElseThrow(() -> new NotFoundException("guardian", guardianUniqueCode, "코드와 일치하는 보호자가 존재하지 않습니다."));

        List<CareerHistory> careerHistories = careerHistoryDescriptions.stream()
                .map(careerHistoryDescription ->
                        CareerHistory.builder()
                                .description(careerHistoryDescription)
                                .build()
                )
                .toList();

        Caregiver caregiver = Caregiver.builder()
                .name(name)
                .phone(phone)
                .hospital(hospital)
                .kakaoId(999L) //TODO: 임시 값
                .mannerScore(0.0)
                .careerHistories(careerHistories)
                .build();
        caregiver.setPatient(guardian.getPatient());
        caregiver.addCareerHistories(careerHistories);

        return caregiver;
    }
}
