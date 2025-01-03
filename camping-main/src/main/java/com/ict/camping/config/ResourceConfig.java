package com.ict.camping.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer{

  public void addResourceHandlers(ResourceHandlerRegistry registry){
    // "/upload/" 경로
    registry.addResourceHandler("/upload/")
            .addResourceLocations("file:C:/Users/5/Desktop/camping-main/camping-main/src/main/resources/images/")
            .setCachePeriod(3600); // 캐시 시간(초)

    // "/uploads/" 경로
    registry.addResourceHandler("/uploads/")
            .addResourceLocations("file:C:/Users/5/Desktop/camping-main/camping-main/src/main/resources/images/")
            .setCachePeriod(3600); // 캐시 시간(초)
  }

}
