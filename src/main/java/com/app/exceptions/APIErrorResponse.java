package com.app.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APIErrorResponse {

  private boolean success;
  @JsonIgnore private APIErrorCode errorCode;
  private String status;

  @JsonProperty("failure_reason")
  private String failureReason;

  @JsonProperty("user_message")
  private String userMessage;

  private LocalDateTime timestamp;

  @JsonProperty("api_details")
  private List<String> details;

  @JsonProperty("reason_code")
  private String reasonCode;

  public APIErrorResponse() {}

  public APIErrorResponse(APIErrorCode apiErrorCode, String failureReason) {
    this.status = apiErrorCode.getHttpStatus().name();
    this.reasonCode =
        apiErrorCode.getReasonCode() != null ? apiErrorCode.getReasonCode().name() : this.status;
    this.success = false;
    this.failureReason = failureReason;
    this.userMessage = apiErrorCode.getUserMessage();
    this.details = List.of();
    this.timestamp = LocalDateTime.now();
  }

  public APIErrorResponse(
      APIErrorCode apiErrorCode, String failureReason, List<String> apiDetails) {
    this.status = apiErrorCode.getHttpStatus().name();
    this.reasonCode =
        apiErrorCode.getReasonCode() != null ? apiErrorCode.getReasonCode().name() : this.status;
    this.success = false;
    this.failureReason = failureReason;
    this.userMessage = apiErrorCode.getUserMessage();
    this.details = apiDetails;
    this.timestamp = LocalDateTime.now();
  }
}
