package com.gussoft.credits.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

  @Value("${config.customers.endpoint}")
  private String url;

  @Bean
  //@LoadBalanced
  public WebClient.Builder registerWebClient() {
    return  WebClient.builder().baseUrl(url);
  }

}
