package com.iiie.server.type;

import lombok.Getter;

@Getter
public enum RelationType {
  PARENT("부모"),
  CHILD("자녀"),
  SIBLING("형제자매"),
  GRANDCHILD("손자/손녀"),
  IN_LAW("사위,며느리"),
  SPOUSE("배우자"),
  RELATIVE("친적");

  private final String value;

  RelationType(String value) {
    this.value = value;
  }

  public static RelationType fromString(String stringRelation) {
    for (RelationType value : RelationType.values()) {
      if (value.getValue().equals(stringRelation)) {
        return value;
      }
    }
    throw new IllegalArgumentException("존재하지 않는 관계명입니다 : " + stringRelation);
  }
}
