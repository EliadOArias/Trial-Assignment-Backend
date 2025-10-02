package com.github.eliadoarias.tgb.controller;

import com.github.eliadoarias.tgb.dto.*;
import com.github.eliadoarias.tgb.result.AjaxResult;
import com.github.eliadoarias.tgb.service.ConfessionService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
     * 发布自己的表白
     * 可能的异常：
     * INVALID_PARAMETERS(2003, "参数错误"),
     * TOKEN_EXP(2201, "token已过期"),
     * TOKEN_MISTAKE(2202, "token错误"),
     * UNAUTHORIZED(2002, "无权访问"),
     * @param dto 数据包
     * @return
     */
    @PreAuthorize("hasAuthority('permission:user.upload')")
    @PostMapping
    public AjaxResult<PostInfo> send(
            @Valid @RequestBody PostCreateRequest dto,
            HttpServletRequest request
    ) {
        String userId = request.getAttribute("user_id").toString();
        return AjaxResult.success(confessionService.send(dto,userId));
    }

    /**
     * 删除自己的表白
     * 可能的异常：
     * POST_NOT_FOUND(404, "目标表白不存在"),
     * TOKEN_EXP(2201, "token已过期"),
     * TOKEN_MISTAKE(2202, "token错误"),
     * UNAUTHORIZED(2002, "无权访问"),
     * POST_UPDATE_NOOP(2703,"无权限修改这个帖子，因为这个帖子不是你发布且你不是管理员")
     * @param confessionId 表白id
     * @return null
     */
    @PreAuthorize("hasAuthority('permission:user.upload')")
    @DeleteMapping("/{id}")
    public AjaxResult<Object> delete(
            @PathVariable("id") Integer confessionId,
            HttpServletRequest request
    ) {
        String userId = request.getAttribute("user_id").toString();
        return AjaxResult.success(confessionService.delete(confessionId, userId));
    }

    /**
     * 修改自己的表白
     * 可能的异常：
     * INVALID_PARAMETERS(2003, "参数错误"),
     * POST_NOT_FOUND(404, "目标表白不存在"),
     * TOKEN_EXP(2201, "token已过期"),
     * TOKEN_MISTAKE(2202, "token错误"),
     * UNAUTHORIZED(2002, "无权访问"),
     * POST_UPDATE_NOOP(2703,"无权限修改这个帖子，因为这个帖子不是你发布且你不是管理员")
     * @param confessionId 表白id
     * @return null
     */
    @PreAuthorize("hasAuthority('permission:user.upload')")
    @PutMapping("/{id}")
    public AjaxResult<PostInfo> update(
            @Valid @PathVariable("id") Integer confessionId,
            @RequestBody PostUpdateRequest dto,
            HttpServletRequest request
    ) {
        String userId = request.getAttribute("user_id").toString();
        return AjaxResult.success(confessionService.update(confessionId,dto,userId));
    }

    /**
     * 点赞
     * 点赞功能，toggle接口。将点赞状态反转（已点赞时取消，未点赞时点赞）
     * 可能的异常：
     * POST_NOT_FOUND(404, "目标表白不存在"),
     * TOKEN_EXP(2201, "token已过期"),
     * TOKEN_MISTAKE(2202, "token错误"),
     * UNAUTHORIZED(2002, "无权访问")
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
     * 获取表白列表
     * 获取帖子，以页码形式获取。输入包含页码和每页的数量，返回包含总页数。
     * 可能的异常：
     * INVALID_PARAMETERS(2003, "参数错误"),
     * TOKEN_EXP(2201, "token已过期"),
     * TOKEN_MISTAKE(2202, "token错误"),
     * UNAUTHORIZED(2002, "无权访问")
     * @param dto 数据包
     * @param request 请求
     * @return data为页信息
     */
    @PreAuthorize("hasAuthority('permission:user.read')")
    @GetMapping
    public AjaxResult<PageInfo> getList(
            @Valid @RequestBody PostGetRequest dto,
            HttpServletRequest request
    ) {
        String userId = request.getAttribute("user_id").toString();
        return AjaxResult.success(confessionService.getList(dto.getPage(), dto.getSize(), userId));
    }

    /**
     * 获取表白热度榜单
     * 获取帖子，以页码形式获取。输入包含页码和每页的数量，返回包含总页数。
     * 可能的异常：
     * INVALID_PARAMETERS(2003, "参数错误"),
     * TOKEN_EXP(2201, "token已过期"),
     * TOKEN_MISTAKE(2202, "token错误"),
     * UNAUTHORIZED(2002, "无权访问")
     * @param dto 数据包
     * @param request 请求
     * @return data为热度榜单
     */
    @PreAuthorize("hasAuthority('permission:user.read')")
    @GetMapping("/hot")
    public AjaxResult<PageInfo> getHotList(
            @Valid @RequestBody PostGetRequest dto,
            HttpServletRequest request
    ) {
        String userId = request.getAttribute("user_id").toString();
        return AjaxResult.success(confessionService.getHotList(dto.getPage(), dto.getSize(), userId));
    }

    /**
     * 获取自己的表白
     * 获取帖子，以页码形式获取。输入包含页码和每页的数量，返回包含总页数。
     * 可能的异常：
     * INVALID_PARAMETERS(2003, "参数错误"),
     * TOKEN_EXP(2201, "token已过期"),
     * TOKEN_MISTAKE(2202, "token错误"),
     * UNAUTHORIZED(2002, "无权访问")
     * @param dto 数据包
     * @param request 请求
     * @return data为自己的表白
     */
    @PreAuthorize("hasAuthority('permission:user.read')")
    @GetMapping("/my")
    public AjaxResult<PageInfo> getMyList(
            @Valid @RequestBody PostGetRequest dto,
            HttpServletRequest request
    ) {
        String userId = request.getAttribute("user_id").toString();
        return AjaxResult.success(confessionService.getHotList(dto.getPage(), dto.getSize(), userId));
    }

    /**
     * 获取某一表白
     * 显示更详细的信息，包括评论情况。
     * 可能的异常：
     * POST_NOT_FOUND(404, "目标表白不存在"),
     * TOKEN_EXP(2201, "token已过期"),
     * TOKEN_MISTAKE(2202, "token错误"),
     * UNAUTHORIZED(2002, "无权访问")
     * @param request 请求
     * @return data为自己的表白
     */
    @PreAuthorize("hasAuthority('permission:user.read')")
    @GetMapping("/{id}")
    public AjaxResult<PostDetailInfo> get(
            @PathVariable("id") Integer postId,
            HttpServletRequest request
    ) {
        String userId = request.getAttribute("user_id").toString();
        return AjaxResult.success(confessionService.getDetail(postId, userId));
    }

    /**
     * 发布评论
     * 可能的异常：
     * INVALID_PARAMETERS(2003, "参数错误"),
     * POST_NOT_FOUND(404, "目标表白不存在"),
     * TOKEN_EXP(2201, "token已过期"),
     * TOKEN_MISTAKE(2202, "token错误"),
     * UNAUTHORIZED(2002, "无权访问")
     * @param postId 回复帖子id
     * @param dto 数据包
     * @param request 请求
     * @return 返回data为评论信息
     */
    @PreAuthorize("hasAuthority('permission:user.read')")
    @PostMapping("/{id}/comments")
    public AjaxResult<CommentInfo> comment(
            @PathVariable("id") Integer postId,
            @Valid @RequestBody CommentRequest dto,
            HttpServletRequest request
    ) {
        String userId = request.getAttribute("user_id").toString();
        return AjaxResult.success(confessionService.sendComment(dto,postId,userId));
    }

    /**
     * 回复评论
     * 可能的异常：
     * INVALID_PARAMETERS(2003, "参数错误"),
     * COMMENT_NOT_FOUND(404, "目标评论不存在"),
     * TOKEN_EXP(2201, "token已过期"),
     * TOKEN_MISTAKE(2202, "token错误"),
     * UNAUTHORIZED(2002, "无权访问")
     * @param commentId 回复评论id
     * @param dto 数据包
     * @param request 请求
     * @return 返回data为评论信息
     */
    @PreAuthorize("hasAuthority('permission:user.read')")
    @PostMapping("/{id}/replies")
    public AjaxResult<CommentInfo> replies(
            @Valid @PathVariable("id") Integer commentId,
            @RequestBody RepliesRequest dto,
            HttpServletRequest request
    ) {
        String userId = request.getAttribute("user_id").toString();
        return AjaxResult.success(confessionService.repliesComment(dto,commentId,userId));
    }
}
