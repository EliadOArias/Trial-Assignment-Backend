package com.github.eliadoarias.tgb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.eliadoarias.tgb.entity.Confession;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cursor.Cursor;

/**
* @author EArias
* @description 针对表【confession】的数据库操作Mapper
* @createDate 2025-09-28 16:41:52
* @Entity com.github.eliadoarias.tgb.entity.Confession
*/
public interface ConfessionMapper extends BaseMapper<Confession> {
    @Select("select * from confession")
    Cursor<Confession> selectCursor();

}




