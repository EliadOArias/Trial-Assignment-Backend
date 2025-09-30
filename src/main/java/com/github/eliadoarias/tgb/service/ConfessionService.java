package com.github.eliadoarias.tgb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.eliadoarias.tgb.dto.*;
import com.github.eliadoarias.tgb.entity.Confession;

import java.util.List;

/**
* @author EArias
* @description 针对表【confession】的数据库操作Service
* @createDate 2025-09-28 16:41:52
*/
public interface ConfessionService extends IService<Confession> {
    PostInfo send(PostCreateRequest dto, String userId);
    Object delete(Integer confessionId, String userId);
    PostInfo update(Integer confessionId, PostUpdateRequest dto, String userId);
    PostInfo like(String userId, Integer postId);
    PageInfo getList(Integer page, Integer size, String userId);
    PageInfo getHotList(Integer page, Integer size, String userId);
    PostDetailInfo getDetail(Integer confessionId, String userId);
    PageInfo getMyList(Integer page, Integer size, String userId);
    List<PostInfo> getListByCursor(Integer cursor);
    CommentInfo sendComment(CommentRequest dto, Integer postId, String userId);
    CommentInfo repliesComment(RepliesRequest dto, Integer commentId, String userId);
}
