package nz.westpac.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  @Value("${http.connection.timeout}")
  private int connectionTimeout;

  @Value("${http.read.timeout}")
  private int readTimeout;

  @Bean
  public RestTemplate restTemplate() {
    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
            = new HttpComponentsClientHttpRequestFactory();
    clientHttpRequestFactory.setConnectTimeout(connectionTimeout);
    clientHttpRequestFactory.setReadTimeout(readTimeout);
    RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
    //basicRestTemplate.setErrorHandler(new DefaultErrorHandler());
    return restTemplate;
  }
}
