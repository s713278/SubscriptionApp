package com.app.security;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserClaims {

  private String userId;
  private String storeId;
}
