package com.github.eliadoarias.tgb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.eliadoarias.tgb.dto.PostCreateRequest;
import com.github.eliadoarias.tgb.dto.PostInfo;
import com.github.eliadoarias.tgb.entity.Confession;
import com.github.eliadoarias.tgb.entity.Likes;
import com.github.eliadoarias.tgb.entity.SendJob;
import com.github.eliadoarias.tgb.mapper.ConfessionMapper;
import com.github.eliadoarias.tgb.mapper.LikesMapper;
import com.github.eliadoarias.tgb.mapper.SendJobMapper;
import com.github.eliadoarias.tgb.service.ConfessionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
        return PostInfo.of(confession);
    }

    @Transactional
    @Override
    public PostInfo like(String userId, Integer postId) {
        List<Likes> likes = likesMapper.selectList(
                new LambdaQueryWrapper<Likes>().eq(Likes::getPostId, postId).eq(Likes::getUserId, userId)
        );
        if(!likes.isEmpty()) {
            log.info("已经点赞过了。");
            return null;
        }
        likesMapper.insert(Likes.builder().postId(postId).userId(userId).build());
        Confession confession = baseMapper.selectById(postId);
        confession.setLikes(confession.getLikes() + 1);
        baseMapper.updateById(confession);
        return PostInfo.of(confession);
    }
}




