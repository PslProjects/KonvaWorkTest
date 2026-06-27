package com.scada;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Favicon.ico request ko blank resource pe redirect kar do
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/emptyfavicon/");
    }
}
