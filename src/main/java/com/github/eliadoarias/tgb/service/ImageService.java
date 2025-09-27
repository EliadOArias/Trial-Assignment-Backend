package com.github.eliadoarias.tgb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.eliadoarias.tgb.dto.FileInfo;
import com.github.eliadoarias.tgb.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService extends IService<Image>{
    FileInfo saveImage(MultipartFile file) throws IOException;
}
