package com.github.eliadoarias.tgb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "mypath")
@Data
@Component
public class PathConfig {
    private String rootPath;
    private String rootUrl;
}
