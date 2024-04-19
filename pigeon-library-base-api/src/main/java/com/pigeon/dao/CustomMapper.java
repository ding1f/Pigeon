package com.pigeon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;

@Mapper
public interface CustomMapper<T> extends BaseMapper<T> {

    /*
     * 真实删除
     */
    int trueDelete(@Param("tableName") String tableName, @Param("objectId") Serializable objectId);

    /*
     * 检查字段是否重复
     */
    int checkUnique(@Param("tableName") String tableName, @Param("key") String key, @Param("value") Object value);
}
