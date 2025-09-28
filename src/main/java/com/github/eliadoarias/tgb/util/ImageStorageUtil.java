package com.github.eliadoarias.tgb.util;

import com.github.eliadoarias.tgb.config.PathConfig;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class ImageStorageUtil {
    @Resource
    PathConfig pathConfig;
    public final static String IMAGE_FOLDER = "resources/image/";

    public String getImageFolderPath(){
        String path = pathConfig.getRootPath()+IMAGE_FOLDER;
        File folder = new File(path);
        if(!folder.isDirectory()){
            folder.mkdirs();
        }
        return path;
    }

    public String buildUrl(String url) {
        return pathConfig.getRootUrl() + url;
    }

    public File saveImage(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString() +
                fileName.substring(fileName.lastIndexOf("."));
        File newFile = new File(getImageFolderPath()+newName);
        file.transferTo(newFile);
        return newFile;
    }
}
