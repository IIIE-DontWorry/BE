package com.iiie.server.type;

import lombok.Getter;

@Getter
public enum TakenStatus {
  TAKEN("복용"),
  NOT_TAKEN("미복용");

  private final String value;

  TakenStatus(String value) {
    this.value = value;
  }

  public static TakenStatus fromString(String stringStatus) {
    for (TakenStatus value : TakenStatus.values()) {
      if (value.getValue().equals(stringStatus)) {
        return value;
      }
    }
    throw new IllegalArgumentException("존재하지 않는 상태명입니다 : " + stringStatus);
  }
}
