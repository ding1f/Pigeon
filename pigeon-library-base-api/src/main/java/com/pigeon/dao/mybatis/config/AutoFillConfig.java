package com.pigeon.dao.mybatis.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.pigeon.context.UserHolder;
import com.pigeon.entity.po.BasePO;
import com.pigeon.utils.SnowflakeIdWorker;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 自动填充属性配置
 * 这个自动填充属性，只会在调用mybatis plus自带的BaseMapper中的方法时才会触发
 * 手动触发请使用：
 * MetaObject metaObject = SystemMetaObject.forObject(需要填充的entity);
 * autoFillConfig.insertFill(metaObject);
 *
 * @author Idenn
 * @date 3/12/2024 11:53 PM
 */
@Component
public class AutoFillConfig implements MetaObjectHandler {

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    //使用mp实现添加的操作，这个方法就会执行
    @Override
    public void insertFill(MetaObject metaObject) {
        if (StringUtils.isEmpty(((BasePO) metaObject.getOriginalObject()).getObjectId())) {
            this.setFieldValByName("objectId", snowflakeIdWorker.nextId(), metaObject);
        }
        Date date = new Date();
        if (UserHolder.getUser() != null) {
            this.setFieldValByName("createdBy", UserHolder.getUser().getUsername(), metaObject);
        }
        this.setFieldValByName("createdTime", date, metaObject);
        if (UserHolder.getUser() != null) {
            this.setFieldValByName("updatedBy", UserHolder.getUser().getUsername(), metaObject);
        }
        this.setFieldValByName("updatedTime", date, metaObject);

        //设置版本号revision的初始值为1
        //不加这个也可以，revision的默认值为null，加了就是设置revision的值从1开始
        this.setFieldValByName("revision", 1, metaObject);

        //添加deleted的初始值为0，也就是默认值
        this.setFieldValByName("isDeleted", "0", metaObject);
    }

    //使用mp实现修改的操作，这个方法就会执行
    @Override
    public void updateFill(MetaObject metaObject) {
        if (UserHolder.getUser() != null) {
            this.setFieldValByName("updatedBy", UserHolder.getUser().getUsername(), metaObject);
        }
        this.setFieldValByName("updatedTime", new Date(), metaObject);
    }
}