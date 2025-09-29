package com.github.eliadoarias.tgb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.eliadoarias.tgb.constant.ExceptionEnum;
import com.github.eliadoarias.tgb.dto.PageInfo;
import com.github.eliadoarias.tgb.dto.PostCreateRequest;
import com.github.eliadoarias.tgb.dto.PostInfo;
import com.github.eliadoarias.tgb.entity.Confession;
import com.github.eliadoarias.tgb.entity.Likes;
import com.github.eliadoarias.tgb.entity.SendJob;
import com.github.eliadoarias.tgb.exception.ApiException;
import com.github.eliadoarias.tgb.mapper.ConfessionMapper;
import com.github.eliadoarias.tgb.mapper.LikesMapper;
import com.github.eliadoarias.tgb.mapper.SendJobMapper;
import com.github.eliadoarias.tgb.service.ConfessionService;
import com.github.eliadoarias.tgb.util.ExceptionUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
* @author EArias
* @description 针对表【confession】的数据库操作Service实现
* @createDate 2025-09-28 16:41:52
*/
@Service
@Slf4j
public class ConfessionServiceImpl extends ServiceImpl<ConfessionMapper, Confession>
    implements ConfessionService{

    @Resource
    private SendJobMapper sendJobMapper;

    @Resource
    private LikesMapper likesMapper;

    @Scheduled(fixedRate = 6000)
    public void trySend() {
        List<SendJob> sendJobs = sendJobMapper.selectList(
                new LambdaQueryWrapper<SendJob>()
                        .orderByAsc(SendJob::getSendTime)
        );
        for (SendJob sendJob : sendJobs) {
            if (sendJob.getSendTime().isBefore(LocalDateTime.now())) {
                log.info("send {}", sendJob.getSendId());
                Confession confession = baseMapper.selectById(sendJob.getSendId());
                confession.setUnsent(false);
                baseMapper.updateById(confession);
                sendJobMapper.deleteById(sendJob.getId());
            } else {
                break;
            }
        }
    }

    private PostInfo createPostInfo(Confession confession, String userId) {
        Integer postId = confession.getId();
        Likes likes = likesMapper.selectOne(
                new LambdaQueryWrapper<Likes>().eq(Likes::getPostId, postId).eq(Likes::getUserId, userId)
        );
        return createPostInfo(confession, Objects.isNull(likes));
    }

    private PostInfo createPostInfo(Confession confession, boolean liked) {
        PostInfo postInfo = PostInfo.of(confession);
        postInfo.setLiked(liked);
        return postInfo;
    }


    @Override
    public PostInfo send(PostCreateRequest dto, String userId) {
        Confession confession = Confession.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .photos(String.join(",", dto.getPhotos()))
                .posterId(userId)
                .views(0)
                .likes(0)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .unsent(false)
                .build();
        if(dto.getSendTime().isAfter(LocalDateTime.now())){
            confession.setUnsent(true);
            baseMapper.insert(confession);
            sendJobMapper.insert(SendJob
                    .builder()
                    .sendId(confession.getId())
                    .sendTime(dto.getSendTime())
                    .build()
            );
        }
        else {
            baseMapper.insert(confession);
        }
        return createPostInfo(confession, userId);
        //PostInfo.of(confession);
    }

    @Transactional
    @Override
    public PostInfo like(String userId, Integer postId) {
        List<Likes> likes = likesMapper.selectList(
                new LambdaQueryWrapper<Likes>().eq(Likes::getPostId, postId).eq(Likes::getUserId, userId)
        );
        if(!likes.isEmpty()) {
            Confession confession = baseMapper.selectById(likes.get(0).getPostId());
            if (Objects.isNull(confession)) throw new ApiException(ExceptionEnum.NOT_FOUND);
            confession.setLikes(confession.getLikes() - 1);
            baseMapper.updateById(confession);
            likesMapper.deleteById(likes.get(0).getId());
            log.info("{} dislike {}", userId, postId);
            return createPostInfo(confession, false);
        }
        likesMapper.insert(Likes.builder().postId(postId).userId(userId).build());
        Confession confession = baseMapper.selectById(postId);
        confession.setLikes(confession.getLikes() + 1);
        baseMapper.updateById(confession);
        log.info("{} like {}", userId, postId);
        return createPostInfo(confession, true);
    }

    @Override
    public PageInfo getList(Integer page, Integer size, String userId) {
        Page<Confession> confessionPage = new Page<>(page, size);
        Page<Confession> result = baseMapper.selectPage(
                confessionPage,
                new LambdaQueryWrapper<Confession>()
                        .orderByDesc(Confession::getUpdateAt)
                        .eq(Confession::isUnsent, false)
        );
        List<PostInfo> postInfos = new ArrayList<>();
        for(Confession confession: result.getRecords()){
            postInfos.add(createPostInfo(confession, userId));
        }
        return PageInfo.builder()
                .posts(postInfos)
                .total(result.getTotal())
                .pages(result.getPages())
                .current(result.getCurrent())
                .build();
    }

    @Override
    public PageInfo getHotList(Integer page, Integer size, String userId) {
        Page<Confession> confessionPage = new Page<>(page, size);
        Page<Confession> result = baseMapper.selectPage(
                confessionPage,
                new QueryWrapper<Confession>()
                        .orderByDesc("likes + views")
                        .eq("unsent", false)
        );
        List<PostInfo> postInfos = new ArrayList<>();
        for(Confession confession: result.getRecords()){
            postInfos.add(createPostInfo(confession, userId));
        }
        return PageInfo.builder()
                .posts(postInfos)
                .total(result.getTotal())
                .pages(result.getPages())
                .current(result.getCurrent())
                .build();
    }

    @Override
    public List<PostInfo> getListByCursor(Integer cursor) {
        return null;
    }
}




