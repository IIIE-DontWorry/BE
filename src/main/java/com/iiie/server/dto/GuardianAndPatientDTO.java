package com.iiie.server.dto;

import com.iiie.server.dto.GuardianDTO.CreationGuardian;
import com.iiie.server.dto.PatientDTO.CreationPatient;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

public class GuardianAndPatientDTO {

  @Getter
  @Setter
  public static class CreationRequest {
    @Schema(
        description = "Guardian creation data",
        example = "{ \"name\": \"이름\", \"phone\": \"01030915525\", \"address\": \"김포\" }")
    private CreationGuardian creationGuardian;

    @Schema(
        description = "Patient creation data",
        example =
            "{ \"name\": \"환자이름\", \"age\": 24, \"diseaseName\": \"암\", \"hospitalName\": \"세브란스\", \"medicationInfos\": [{\"name\": \"약1\"}, {\"name\": \"약2\"}] }")
    private CreationPatient creationPatient;
  }
}
