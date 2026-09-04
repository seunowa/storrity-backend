package com.storrity.storrity;

import com.storrity.storrity.dbbackup.DatabaseBackupProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@OpenAPIDefinition(
  info = @Info(
    title = "Storrity 1.0 API",
    version = "1.0",
    description = "API documentation for storrity store management application"
  )
)
@SpringBootApplication
@EnableAsync //enable async application events
@EnableConfigurationProperties(DatabaseBackupProperties.class)
public class StorrityApplication {

	public static void main(String[] args) {
		SpringApplication.run(StorrityApplication.class, args);
	}

}
