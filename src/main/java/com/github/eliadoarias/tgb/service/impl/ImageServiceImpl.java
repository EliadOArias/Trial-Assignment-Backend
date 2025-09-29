package com.github.eliadoarias.tgb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.eliadoarias.tgb.config.PathConfig;
import com.github.eliadoarias.tgb.dto.FileInfo;
import com.github.eliadoarias.tgb.entity.Image;
import com.github.eliadoarias.tgb.mapper.ImageMapper;
import com.github.eliadoarias.tgb.service.ImageService;
import com.github.eliadoarias.tgb.util.ImageStorageUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {
    @Resource
    private PathConfig pathConfig;
    @Resource
    ImageStorageUtil imageStorageUtil;
    @Override
    public FileInfo saveImage(MultipartFile file) throws IOException {
        String md5 = DigestUtils.md5DigestAsHex(file.getBytes());

        LambdaQueryWrapper<Image> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Image::getMd5Code,md5);
        Image image = getOne(queryWrapper);

        if (image != null) {
            log.info("重复图片！返回旧图片url");
            return new FileInfo(imageStorageUtil.buildUrl(image.getImageUrl()));
        } else {
            File newFile = imageStorageUtil.saveImage(file);
            String newName = newFile.getName();
            String url = ImageStorageUtil.IMAGE_FOLDER + newName;
            String returnUrl = imageStorageUtil.buildUrl(ImageStorageUtil.IMAGE_FOLDER + newName);

            baseMapper.insert(Image.builder()
                    .imageUrl(url)
                    .md5Code(md5)
                    .build());
            return new FileInfo(returnUrl);
        }
    }
}
