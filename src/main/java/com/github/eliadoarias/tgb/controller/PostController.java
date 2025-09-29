package com.github.eliadoarias.tgb.controller;

import com.github.eliadoarias.tgb.dto.PageInfo;
import com.github.eliadoarias.tgb.dto.PostCreateRequest;
import com.github.eliadoarias.tgb.dto.PostGetRequest;
import com.github.eliadoarias.tgb.dto.PostInfo;
import com.github.eliadoarias.tgb.result.AjaxResult;
import com.github.eliadoarias.tgb.service.ConfessionService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 点赞
     * 点赞功能，toggle接口。将点赞状态反转（已点赞时取消，未点赞时点赞）
     * @param id 帖子ID
     * @param request 请求
     * @return 更新后的点赞状态
     */
    @PreAuthorize("hasAuthority('permission:user.upload')")
    @PostMapping("/{id}/like")
    public AjaxResult<PostInfo> like(
            @PathVariable("id") Integer id,
            HttpServletRequest request
    ) {
        String userId = request.getAttribute("user_id").toString();
        return AjaxResult.success(confessionService.like(userId,id));
    }

    /**
     * 获取表白
     * 获取帖子，以页码形式获取。输入包含页码和每页的数量，返回包含总页数。
     * @param dto 数据包
     * @param request 请求
     * @return data为页信息
     */
    @PreAuthorize("hasAuthority('permission:user.read')")
    @GetMapping
    public AjaxResult<PageInfo> getList(
            @RequestBody PostGetRequest dto,
            HttpServletRequest request
    ) {
        String userId = request.getAttribute("user_id").toString();
        return AjaxResult.success(confessionService.getList(dto.getPage(), dto.getSize(), userId));
    }

    /**
     * 获取表白热度榜单
     * 获取帖子，以页码形式获取。输入包含页码和每页的数量，返回包含总页数。
     * @param dto 数据包
     * @param request 请求
     * @return data为热度榜单
     */
    @PreAuthorize("hasAuthority('permission:user.read')")
    @GetMapping("/hot")
    public AjaxResult<PageInfo> getHotList(
            @RequestBody PostGetRequest dto,
            HttpServletRequest request
    ) {
        String userId = request.getAttribute("user_id").toString();
        return AjaxResult.success(confessionService.getHotList(dto.getPage(), dto.getSize(), userId));
    }
}
