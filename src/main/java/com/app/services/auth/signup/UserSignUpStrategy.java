package com.app.services.auth.signup;

import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.request.EmailSignUpRequest;
import com.app.payloads.request.MobileSignUpRequest;
import com.app.payloads.request.SignUpRequest;
import com.app.payloads.response.SignUpDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class UserSignUpStrategy {
  private final SignUpServiceFactory signUpServiceFactory;

  public SignUpDTO processUserSignUp(SignUpRequest signUpRequest) {
    if (signUpRequest instanceof MobileSignUpRequest) {
      return signUpServiceFactory.get("mobileSignUpService").processSignUp(signUpRequest);
    }
    if (signUpRequest instanceof EmailSignUpRequest) {
      return signUpServiceFactory.get("emailSignUpService").processSignUp(signUpRequest);
    }
    throw new APIException(APIErrorCode.API_400, "No SignUpService Found");
  }
}
