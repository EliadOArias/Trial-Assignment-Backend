package com.github.eliadoarias.tgb.config;


import com.github.eliadoarias.tgb.service.ImageService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourcesConfig implements WebMvcConfigurer
{
    @Resource
    private PathConfig pathConfig;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/resources/**").addResourceLocations("file:/"+pathConfig.getRootPath());
    }
}
