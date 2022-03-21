package com.graphql.demo;

import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class AppAuthenticationSuccessHandler  extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  public AppAuthenticationSuccessHandler() {
    super();
    setUseReferer(true);
  }
  
}