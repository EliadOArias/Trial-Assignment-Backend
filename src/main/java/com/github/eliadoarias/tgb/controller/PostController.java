package com.github.eliadoarias.tgb.controller;

import com.github.eliadoarias.tgb.dto.PostCreateRequest;
import com.github.eliadoarias.tgb.dto.PostInfo;
import com.github.eliadoarias.tgb.result.AjaxResult;
import com.github.eliadoarias.tgb.service.ConfessionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 表白墙
 */
@RestController
@RequestMapping("/api/confessions")
@Slf4j
public class PostController {
    @Resource
    private ConfessionService confessionService;

    /**
     * 发布
     * @param dto 数据包
     * @param userId 用户uid
     * @return
     */
    @PreAuthorize("hasAuthority('permission:user.upload')")
    @PostMapping
    public AjaxResult<PostInfo> send(
            @RequestBody PostCreateRequest dto,
            @RequestAttribute("user_id") String userId
    ) {
        return AjaxResult.success(confessionService.send(dto,userId));
    }
}
