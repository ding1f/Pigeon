package com.pigeon.service.impl;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.pigeon.exception.BaseException;
import com.pigeon.service.BaseService;
import com.pigeon.dao.CustomMapper;
import com.pigeon.interfaces.annotation.IsUnique;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * 包含了service实现类的共通方法
 *
 * @author Idenn
 * @date 3/16/2024 6:43 PM
 */
@Service
public class BaseServiceImpl<T> implements BaseService<T> {

    @Resource
    private CustomMapper<T> customMapper;

    /**
     * 真实删除
     *
     * @param entity 需要删除的PO类型
     * @return int
     * @author Idenn
     * @date 3/16/2024 8:01 PM
     */
    @Override
    public int trueDelete(T entity) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
        if (tableInfo == null) {
            throw new BaseException("获取表信息失败，真实删除失败！");
        }

        Serializable objectId = (Serializable) ReflectionKit.getFieldValue(entity, tableInfo.getKeyProperty());
        return customMapper.trueDelete(tableInfo.getTableName(), objectId);
    }

    /**
     * 检查唯一性
     *
     * @param entity PO
     * @return int
     * @author Idenn
     * @date 3/16/2024 8:08 PM
     */
    @Override
    public void checkUnique(T entity) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
        if (tableInfo == null) {
            throw new BaseException("获取表信息失败，唯一性校验失败！");
        }

        Field[] fields = entity.getClass().getDeclaredFields();
        if (fields.length == 0) {
            return;
        }

        for (Field field : fields) {
            if (field.isAnnotationPresent(IsUnique.class)) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    String key = field.getName();
                    IsUnique isUnique = field.getAnnotation(IsUnique.class);
                    String name = isUnique.name();
                    if (customMapper.checkUnique(tableInfo.getTableName(), key, value) > 0) {
                        throw new BaseException(name + "重复，唯一性校验失败！");
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("唯一性校验失败，请检查字段名字。Error accessing field " + field.getName(), e);
                }
            }
        }
    }
}
