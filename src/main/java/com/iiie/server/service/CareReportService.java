package com.iiie.server.service;

import com.iiie.server.domain.CareReport;
import com.iiie.server.domain.CareSchedule;
import com.iiie.server.domain.Caregiver;
import com.iiie.server.domain.MedicationCheckList;
import com.iiie.server.exception.NotFoundException;
import com.iiie.server.repository.CareGiverRepository;
import com.iiie.server.repository.CareReportRepository;
import com.iiie.server.repository.CareScheduleRepository;
import com.iiie.server.repository.MedicationChecklistRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CareReportService {

    private final CareReportRepository careReportRepository;
    private final CareGiverRepository careGiverRepository;
    private final CareScheduleRepository careScheduleRepository;
    private final MedicationChecklistRepository medicationChecklistRepository;


    public CareReportService(CareReportRepository careReportRepository, CareGiverRepository careGiverRepository,
            CareScheduleRepository careScheduleRepository, MedicationChecklistRepository medicationChecklistRepository) {
        this.careReportRepository = careReportRepository;
        this.careGiverRepository = careGiverRepository;
        this.careScheduleRepository = careScheduleRepository;
        this.medicationChecklistRepository = medicationChecklistRepository;
    }

    @Transactional
    public CareReport createCareReport(Long careGiverID, String specialNote, List<String> description) {
        Caregiver caregiver = careGiverRepository.findById(careGiverID)
                .orElseThrow(() -> new NotFoundException("careReport", careGiverID, "존재하지 않는 간병인입니다."));

        CareReport careReport = CareReport.builder()
                .caregiver(caregiver)
                .specialNote(specialNote)
                .build();
        CareReport savedCareReport = careReportRepository.save(careReport);

        createCareSchedule(description,careReport);
        //updateMedicationCheck(caregiver.getPatient().getId());

        return savedCareReport;

    }

/*    private void updateMedicationCheck(Long patientId) {
        // 투약 리스트가 존재하지 않으면 empty
        // 투약 리스트가 존재하면 불러온다.
        Optional<List<MedicationCheckList>> optionalMedicationCheckLists = medicationChecklistRepository.findAllByPatientId(
                patientId);

        if (optionalMedicationCheckLists.isEmpty()) {
            return ;
        }

        List<MedicationCheckList> medicationCheckLists = optionalMedicationCheckLists.get();
        // TODO: DTO로 받아와야할듯? 아닌가 좀 생각해보자.
        물어볼거있으면 헤드셋 벗고 있어서 채팅으로 먼저 언급해주세요오
    }*/

    private void createCareSchedule(List<String> descriptions, CareReport careReport) {

        List<CareSchedule> careSchedules = descriptions.stream()
                .map(description -> {
                    CareSchedule careSchedule = CareSchedule.builder()
                            .description(description)
                            .build();
                    careSchedule.setCareReport(careReport); // 연관관계 보조 메서드 호출
                    return careSchedule;
                })
                .toList();

        careScheduleRepository.saveAll(careSchedules);
    }
}
