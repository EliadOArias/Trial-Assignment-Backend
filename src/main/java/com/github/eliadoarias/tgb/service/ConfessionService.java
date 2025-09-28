package com.github.eliadoarias.tgb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.eliadoarias.tgb.dto.PostCreateRequest;
import com.github.eliadoarias.tgb.dto.PostInfo;
import com.github.eliadoarias.tgb.entity.Confession;

/**
* @author EArias
* @description 针对表【confession】的数据库操作Service
* @createDate 2025-09-28 16:41:52
*/
public interface ConfessionService extends IService<Confession> {
    PostInfo send(PostCreateRequest dto, String userId);
    PostInfo like(String userId, Integer postId);
}
