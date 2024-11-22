package com.iiie.server.exception;

public class AlreadyExistException extends RuntimeException {

  private final String resourceType;
  private final Object resourceId;

  public AlreadyExistException(String resourceType, Object resourceId, String message) {
    super(message);
    this.resourceType = resourceType;
    this.resourceId = resourceId;
  }

  public String getResourceType() {
    return this.resourceType;
  }

  public Object getResourceId() {
    return this.resourceId;
  }
}
