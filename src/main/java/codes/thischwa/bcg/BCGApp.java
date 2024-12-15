package codes.thischwa.bcg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
@Slf4j
public class BCGApp {
  public static void main(String[] args) {
    try {
      new SpringApplicationBuilder(BCGApp.class).web(WebApplicationType.NONE).run(args);
    } catch (Exception e) {
      log.error("Unexpected exception, Spring Boot stops! Message: {}", e.getMessage());
      System.exit(10);
    }
  }
}
