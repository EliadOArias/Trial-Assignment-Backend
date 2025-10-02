package com.github.eliadoarias.tgb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件数据
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileInfo {
    /**
     * 文件供给前端访问的url路径，一般为静态资源
     * @titleName 文件url
     * @mock http://localhost:8080/resources/image/@word().png
     */
    @JsonProperty("file_url")
    @NotBlank
    @NotNull
    private String fileUrl;
}
