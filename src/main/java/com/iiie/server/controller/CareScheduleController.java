package com.iiie.server.controller;

import com.iiie.server.dto.CareReportDTO.CareReportResponse.CareScheduleResponse;
import com.iiie.server.dto.CareScheduleDTO.PostCareScheduleRequest;
import com.iiie.server.service.CareScheduleService;
import com.iiie.server.utils.SuccessResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/care-schedules")
public class CareScheduleController {

  private final CareScheduleService careScheduleService;

  public CareScheduleController(CareScheduleService careScheduleService) {
    this.careScheduleService = careScheduleService;
  }

  @PostMapping("/{careReportId}")
  public SuccessResponse<CareScheduleResponse> createCareSchedule(
      @RequestBody PostCareScheduleRequest request,
      @PathVariable(name = "careReportId") Long careReportId) {
    CareScheduleResponse careScheduleResponse =
        careScheduleService.createCareSchedule(
            careReportId, request.getHour(), request.getMinute(), request.getDescription());

    return new SuccessResponse<>("시간에 따른 일지를 추가하였습니다.", careScheduleResponse);
  }
}
