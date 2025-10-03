package com.github.eliadoarias.tgb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.eliadoarias.tgb.constant.ExceptionEnum;
import com.github.eliadoarias.tgb.dto.*;
import com.github.eliadoarias.tgb.entity.*;
import com.github.eliadoarias.tgb.exception.ApiException;
import com.github.eliadoarias.tgb.mapper.*;
import com.github.eliadoarias.tgb.service.ConfessionService;
import com.github.eliadoarias.tgb.service.UserService;
import com.github.eliadoarias.tgb.util.ImageStorageUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.core.env.Environment;
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

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private DataSourceProperties dataSourceProperties;

    @Resource
    private UserService userService;
    @Autowired
    private ImageStorageUtil imageStorageUtil;
    @Resource
    private Environment environment;

    @Scheduled(fixedRate = 6000)
    public void trySend() {
        //log.info("Try connect {} in {} profile",dataSourceProperties.getUrl(),String.join(",", environment.getActiveProfiles()));

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
        List<String> photoAbsoluteUrls = new ArrayList<>();
        if(!confession.getPhotos().isEmpty()) for (String photo : confession.getPhotos().split(",")){
            photoAbsoluteUrls.add(imageStorageUtil.buildUrl(photo));
        }
        PostInfo postInfo = PostInfo.of(confession);
        postInfo.setLiked(Objects.isNull(likes));
        postInfo.setPhotos(photoAbsoluteUrls);
        if(!confession.isAnonymous()) {
            postInfo.setPosterName(user.getUsername());
            postInfo.setName(user.getName());
            postInfo.setAvatar(imageStorageUtil.buildUrl(user.getAvatar()));
        }else {
            postInfo.setAvatar(imageStorageUtil.buildUrl("default.ico"));
        }
        return postInfo;
    }

    private PostDetailInfo createPostDetailInfo(Confession confession, String userId) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUserId, userId)
        );
        Integer postId = confession.getId();
        Likes likes = likesMapper.selectOne(
                new LambdaQueryWrapper<Likes>().eq(Likes::getPostId, postId).eq(Likes::getUserId, user.getId())
        );
        List<String> photoAbsoluteUrls = new ArrayList<>();
        if(!confession.getPhotos().isEmpty()) for (String photo : confession.getPhotos().split(",")){
            photoAbsoluteUrls.add(imageStorageUtil.buildUrl(photo));
        }
        PostDetailInfo postDetailInfo = PostDetailInfo.of(confession);
        postDetailInfo.setLiked(Objects.isNull(likes));
        postDetailInfo.setPhotos(photoAbsoluteUrls);
        if(!confession.isAnonymous()) {
            postDetailInfo.setPosterName(user.getUsername());
            postDetailInfo.setName(user.getName());
            postDetailInfo.setAvatar(imageStorageUtil.buildUrl(user.getAvatar()));
        }else {
            postDetailInfo.setAvatar(imageStorageUtil.buildUrl("default.ico"));
        }
        return postDetailInfo;
    }


    @Override
    public PostInfo send(PostCreateRequest dto, String userId) {
        User user = userMapper.selectOne(
                new  LambdaQueryWrapper<User>().eq(User::getUserId,userId)
        );
        List<String> photoRelativeUrls = new ArrayList<>();
        for(String photo : dto.getPhotos()) {
            photoRelativeUrls.add(imageStorageUtil.getRelativeUrl(photo));
        }
        Confession confession = Confession.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .photos(String.join(",", photoRelativeUrls))
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
    }

    @Override
    public Object delete(Integer confessionId, String userId){
        Confession confession = baseMapper.selectById(confessionId);
        if (Objects.isNull(confession)) throw new ApiException(ExceptionEnum.POST_NOT_FOUND);
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUserId,userId)
        );
        if (!Objects.equals(confession.getPosterId(), user.getId())) throw new ApiException(ExceptionEnum.POST_UPDATE_NOOP);
        baseMapper.deleteById(confession.getId());
        return null;
    }

    @Transactional
    @Override
    public PostInfo update(Integer confessionId, PostUpdateRequest dto, String userId) {
        Confession confession = baseMapper.selectById(confessionId);
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUserId,userId)
        );
        if (Objects.isNull(confession)) throw new ApiException(ExceptionEnum.POST_NOT_FOUND);
        if (!Objects.equals(confession.getPosterId(), user.getId())) throw new ApiException(ExceptionEnum.POST_UPDATE_NOOP);
        if(!Objects.isNull(dto.getOpen()))confession.setOpen(dto.getOpen());
        if(!Objects.isNull(dto.getContent()))confession.setContent(dto.getContent());
        if(!Objects.isNull(dto.getTitle()))confession.setTitle(dto.getTitle());
        List<String> photoRelativeUrls = new ArrayList<>();
        if(!Objects.isNull(dto.getPhotos())){
            for(String photo : dto.getPhotos()) {
                photoRelativeUrls.add(imageStorageUtil.getRelativeUrl(photo));
            }
        }
        if(!Objects.isNull(dto.getPhotos()))confession.setPhotos(String.join(",", photoRelativeUrls));
        baseMapper.updateById(confession);
        return createPostInfo(confession,userId);
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

    @Transactional
    @Override
    public PostDetailInfo getDetail(Integer confessionId, String userId) {
        Confession confession = baseMapper.selectById(confessionId);
        if(Objects.isNull(confession)) throw new ApiException(ExceptionEnum.POST_NOT_FOUND);
        PostDetailInfo postDetailInfo = createPostDetailInfo(confession, userId);
        List<Comment> comments = commentMapper.selectList(
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getPostId,confessionId)
        );
        confession.setViews(confession.getViews() + 1);
        baseMapper.updateById(confession);
        postDetailInfo.setComments(List.copyOf(comments));
        return postDetailInfo;
    }

    @Override
    public PageInfo getMyList(Integer page, Integer size, String userId) {
        User userMe = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserId,userId));
        Page<Confession> confessionPage = new Page<>(page, size);
        Page<Confession> result = baseMapper.selectPage(
                confessionPage,
                new LambdaQueryWrapper<Confession>()
                        .orderByDesc(Confession::getUpdateAt)
                        .eq(Confession::getPosterId, userMe.getId())
        );
        return getPageInfo(userId, result);
    }

    @Override
    public List<PostInfo> getListByCursor(Integer cursor) {
        return null;
    }

    @Override
    public CommentInfo sendComment(CommentRequest dto, Integer postId, String userId) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserId,userId));
        Confession confession = baseMapper.selectById(postId);
        if (Objects.isNull(confession)) throw new ApiException(ExceptionEnum.POST_NOT_FOUND);
        boolean isRoot = false;
        Comment parentComment = null;
        Comment rootComment = null;
        if (Objects.isNull(dto.getParentId())){
            isRoot = true;
        } else {
            parentComment = commentMapper.selectById(dto.getParentId());
            if (!Objects.isNull(parentComment)){
                if(!Objects.isNull(parentComment.getRootId())) {
                    rootComment = commentMapper.selectById(parentComment.getRootId());
                }
            }else {
                isRoot = true;
            }
        }
        Comment comment = Comment.builder()
                .postId(postId)
                .userId(user.getId())
                .rootId(Objects.isNull(rootComment)?-1:rootComment.getId())
                .parentId(Objects.isNull(parentComment)?null:parentComment.getId())
                .content(dto.getContent())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
        commentMapper.insert(comment);
        if(isRoot){
            comment.setRootId(comment.getId());
            commentMapper.updateById(comment);
        }
        CommentInfo commentInfo = CommentInfo.of(comment);
        commentInfo.setUsername(user.getUsername());
        return commentInfo;
    }

    @Override
    public CommentInfo repliesComment(RepliesRequest dto, Integer commentId, String userId) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserId,userId));
        Comment parentComment = commentMapper.selectById(commentId);
        if (Objects.isNull(parentComment)) throw new ApiException(ExceptionEnum.COMMENT_NOT_FOUND);
        Comment rootComment = commentMapper.selectById(parentComment.getRootId());
        if (Objects.isNull(rootComment)) throw new ApiException(ExceptionEnum.COMMENT_NOT_FOUND);
        Comment comment = Comment.builder()
                .postId(parentComment.getPostId())
                .userId(user.getId())
                .rootId(rootComment.getId())
                .parentId(parentComment.getId())
                .content(dto.getContent())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
        commentMapper.insert(comment);
        CommentInfo commentInfo = CommentInfo.of(comment);
        commentInfo.setUsername(user.getUsername());
        return commentInfo;
    }
}




