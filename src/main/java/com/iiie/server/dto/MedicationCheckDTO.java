package com.iiie.server.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class MedicationCheckDTO {

  @Getter
  @Setter
  public static class MedicationCheckCreationRequest {
    private List<String> names;
  }
}
