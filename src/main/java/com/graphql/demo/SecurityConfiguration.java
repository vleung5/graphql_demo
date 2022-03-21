package com.graphql.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Order(1)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  
  @Autowired
  private ApplicationProperties properties;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
  }
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
    .requestMatchers()
    .antMatchers("/oauth/authorize**", "/login**", "/error**", "/oauth2/authorization/**", "/callback**")
    .and()
    .authorizeRequests()
      .antMatchers("/graphql_demo/healthcheck", "/h2/**").permitAll()
    .and()
    .formLogin().permitAll();

  }
   
  @Bean
  public SessionRegistry sessionRegistry() {
      return new SessionRegistryImpl();
  }

  @Bean
  public HttpSessionEventPublisher httpSessionEventPublisher() {
      return new HttpSessionEventPublisher();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
  @Override
  @Bean
  public UserDetailsService userDetailsService() {
    List<UserDetails> userDetailsList = new ArrayList<>();
    userDetailsList.add(User.withUsername(properties.getGraphqlUser()).password(passwordEncoder().encode(properties.getGraphqlPassword())).roles("USER").build());

    return new InMemoryUserDetailsManager(userDetailsList);
  }

}