create table blacklist
(
    id             int auto_increment
        primary key,
    source_user_id int not null,
    target_user_id int not null,
    constraint blacklist_pk_2
        unique (source_user_id, target_user_id)
);

create table comment
(
    id        int auto_increment
        primary key,
    post_id   int                                       not null,
    user_id   int                                       not null,
    parent_id int                                       null,
    root_id   int                                       not null,
    content   varchar(200)                              not null,
    create_at timestamp(3) default CURRENT_TIMESTAMP(3) not null,
    update_at timestamp(3) default CURRENT_TIMESTAMP(3) not null on update CURRENT_TIMESTAMP(3)
);

create table confession
(
    id        int auto_increment
        primary key,
    title     varchar(100)                              not null,
    content   varchar(500)                              not null,
    photos    varchar(200)                              null,
    views     int                                       not null,
    likes     int                                       not null,
    poster_id int                                       not null,
    create_at timestamp(3) default CURRENT_TIMESTAMP(3) not null,
    update_at timestamp(3) default CURRENT_TIMESTAMP(3) not null on update CURRENT_TIMESTAMP(3),
    unsent    tinyint(1)   default 0                    not null,
    open      tinyint(1)   default 1                    not null,
    anonymous tinyint(1)   default 0                    null
);

create table image
(
    id        int auto_increment
        primary key,
    image_url varchar(200) not null,
    md5_code  varchar(200) not null,
    constraint MD5
        unique (md5_code)
);

create table likes
(
    id      int auto_increment
        primary key,
    user_id int not null,
    post_id int not null,
    constraint likes_pk
        unique (user_id, post_id)
);

create table send_job
(
    id        int auto_increment
        primary key,
    send_id   int                                       not null,
    send_time timestamp(3) default CURRENT_TIMESTAMP(3) not null
);

create index send_job_pk
    on send_job (send_time);

create table user
(
    id        int auto_increment
        primary key,
    username  varchar(20)          not null,
    password  varchar(100)         not null,
    name      varchar(20)          not null,
    user_id   varchar(50)          not null,
    usertype  int                  not null,
    avatar    varchar(100)         not null,
    open      tinyint(1) default 1 not null,
    anonymous tinyint(1) default 0 not null,
    constraint user_pk_2
        unique (user_id)
);


