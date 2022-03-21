package com.graphql.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
public class ApplicationProperties {
  
  private String graphqlUser;
  
  private String graphqlPassword;

  public String getGraphqlUser() {
    return graphqlUser;
  }

  public void setGraphqlUser(String graphqlUser) {
    this.graphqlUser = graphqlUser;
  }

  public String getGraphqlPassword() {
    return graphqlPassword;
  }

  public void setGraphqlPassword(String graphqlPassword) {
    this.graphqlPassword = graphqlPassword;
  }
  
  
  
}