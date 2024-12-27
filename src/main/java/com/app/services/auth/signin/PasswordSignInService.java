package com.app.services.auth.signin;

import com.app.entites.Customer;
import com.app.payloads.request.SignInRequest;
import com.app.payloads.response.AuthDetailsDTO;
import com.app.services.auth.dto.AuthUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordSignInService extends AbstractSignInService {
  private final AuthenticationManager authenticationManager;

  @Override
  protected AuthDetailsDTO doSignIn(SignInRequest signInRequest, Customer customer) {
    UsernamePasswordAuthenticationToken authCredentials =
        new UsernamePasswordAuthenticationToken(
            signInRequest.getMobile(), signInRequest.getPassword());
    var authentication = authenticationManager.authenticate(authCredentials);
    var userDetails = (AuthUserDetails) authentication.getPrincipal();
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return getSignInResponse(userDetails);
  }
}
