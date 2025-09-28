package com.github.eliadoarias.tgb.controller;

import com.github.eliadoarias.tgb.dto.FileInfo;
import com.github.eliadoarias.tgb.result.AjaxResult;
import com.github.eliadoarias.tgb.service.ImageService;
import com.github.eliadoarias.tgb.util.ImageStorageUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 图片
 */
@RestController
@RequestMapping("/api/image")
@Slf4j
public class ImageController {

    @Resource
    private ImageService imageService;

    @Resource
    private ImageStorageUtil  imageStorageUtil;

    /**
     * 上传图片
     * 上传一张图片。
     * @param file 图片文件。
     * @return 返回图片文件url。
     */
    @PreAuthorize("hasAuthority('permission:user.upload')")
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
