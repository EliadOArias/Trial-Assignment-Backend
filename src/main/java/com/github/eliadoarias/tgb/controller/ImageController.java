package com.github.eliadoarias.tgb.controller;

import com.github.eliadoarias.tgb.dto.FileInfo;
import com.github.eliadoarias.tgb.result.AjaxResult;
import com.github.eliadoarias.tgb.service.ImageService;
import com.github.eliadoarias.tgb.util.ImageStorageUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/image")
@Slf4j
public class ImageController {

    @Resource
    private ImageService imageService;

    @Resource
    private ImageStorageUtil  imageStorageUtil;

    @PostMapping("/upload")
    public AjaxResult<FileInfo> upload(
            @RequestParam MultipartFile file
    ) throws IOException {
        log.info("Upload file: {}",file.getOriginalFilename());
        return AjaxResult.success(
                imageService.saveImage(file)
        );
    }
}
