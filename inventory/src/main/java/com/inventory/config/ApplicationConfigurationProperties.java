package com.inventory.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = { "classpath:application.properties" })
@ConfigurationProperties(prefix="application")
@Data
public class ApplicationConfigurationProperties {
  private String dateFormat;
  private String dateFormatWithTime;

}
