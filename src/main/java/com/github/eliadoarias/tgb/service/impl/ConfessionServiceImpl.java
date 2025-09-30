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
import com.github.eliadoarias.tgb.entity.User;
import com.github.eliadoarias.tgb.exception.ApiException;
import com.github.eliadoarias.tgb.mapper.ConfessionMapper;
import com.github.eliadoarias.tgb.mapper.LikesMapper;
import com.github.eliadoarias.tgb.mapper.SendJobMapper;
import com.github.eliadoarias.tgb.mapper.UserMapper;
import com.github.eliadoarias.tgb.service.ConfessionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public static final String BlackListSql =
            "select 1 from blacklist b "+
            "where b.source_user_id = {0} "+
            "and b.target_user_id = confession.poster_id";

    @Resource
    private SendJobMapper sendJobMapper;

    @Resource
    private LikesMapper likesMapper;

    @Resource
    private UserMapper userMapper;

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
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUserId, userId)
        );
        Integer postId = confession.getId();
        Likes likes = likesMapper.selectOne(
                new LambdaQueryWrapper<Likes>().eq(Likes::getPostId, postId).eq(Likes::getUserId, user.getId())
        );
        return createPostInfo(confession, Objects.isNull(likes), user.getName());
    }

    private PostInfo createPostInfo(Confession confession, boolean liked, String userName) {
        PostInfo postInfo = PostInfo.of(confession);
        postInfo.setLiked(liked);
        if(confession.isAnonymous())postInfo.setPosterName(userName);
        return postInfo;
    }


    @Override
    public PostInfo send(PostCreateRequest dto, String userId) {
        User user = userMapper.selectOne(
                new  LambdaQueryWrapper<User>().eq(User::getUserId,userId)
        );
        Confession confession = Confession.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .photos(String.join(",", dto.getPhotos()))
                .posterId(user.getId())
                .views(0)
                .likes(0)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .unsent(false)
                .open(dto.isOpen())
                .anonymous(dto.isAnonymous())
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
        User user = userMapper.selectOne(
                new  LambdaQueryWrapper<User>().eq(User::getUserId,userId)
        );
        List<Likes> likes = likesMapper.selectList(
                new LambdaQueryWrapper<Likes>().eq(Likes::getPostId, postId).eq(Likes::getUserId, user.getId())
        );
        if(!likes.isEmpty()) {
            Confession confession = baseMapper.selectById(likes.get(0).getPostId());
            if (Objects.isNull(confession)) throw new ApiException(ExceptionEnum.NOT_FOUND);
            confession.setLikes(confession.getLikes() - 1);
            baseMapper.updateById(confession);
            likesMapper.deleteById(likes.get(0).getId());
            log.info("{} dislike {}", userId, postId);
            return createPostInfo(confession, userId);
        }
        likesMapper.insert(Likes.builder().postId(postId).userId(user.getId()).build());
        Confession confession = baseMapper.selectById(postId);
        confession.setLikes(confession.getLikes() + 1);
        baseMapper.updateById(confession);
        log.info("{} like {}", userId, postId);
        return createPostInfo(confession, userId);
    }

    @Override
    public PageInfo getList(Integer page, Integer size, String userId) {
        User userMe = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserId,userId));
        Page<Confession> confessionPage = new Page<>(page, size);
        Page<Confession> result = baseMapper.selectPage(
                confessionPage,
                new LambdaQueryWrapper<Confession>()
                        .orderByDesc(Confession::getUpdateAt)
                        .eq(Confession::isUnsent, false)
                        .eq(Confession::isOpen, true)
                        .notExists(BlackListSql, userMe.getId())
        );
        return getPageInfo(userId, result);
    }

    private PageInfo getPageInfo(String userId, Page<Confession> result) {
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
        User userMe = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserId,userId));
        Page<Confession> confessionPage = new Page<>(page, size);
        Page<Confession> result = baseMapper.selectPage(
                confessionPage,
                new QueryWrapper<Confession>()
                        .orderByDesc("likes + views")
                        .eq("unsent", false)
                        .eq("open",true)
                        .notExists(BlackListSql, userMe.getId())
        );
        return getPageInfo(userId, result);
    }

    @Override
    public List<PostInfo> getListByCursor(Integer cursor) {
        return null;
    }
}




