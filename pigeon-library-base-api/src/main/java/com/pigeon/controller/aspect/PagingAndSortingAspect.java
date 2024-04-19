package com.pigeon.controller.aspect;

import com.github.pagehelper.PageHelper;
import com.pigeon.entity.vo.BaseQueryVO;
import com.pigeon.enums.OrderEnums;
import com.pigeon.exception.BaseException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * 分页，排序切面
 *
 * @author Idenn
 * @date 3/11/2024 8:25 PM
 */
@Aspect
@Component
public class PagingAndSortingAspect {

    @Before("@within(org.springframework.web.bind.annotation.RestController)")
    public void before(JoinPoint joinPoint) {

        // 跳过无参数方法
        if (joinPoint.getArgs().length == 0) return;

        // 通过jp获取参数列表的第一个
        Object arg = joinPoint.getArgs()[0];
        if (arg instanceof BaseQueryVO) {
            BaseQueryVO baseQueryVO = (BaseQueryVO) joinPoint.getArgs()[0];

            // 判断分页参数是否存在
            if (baseQueryVO.getPageNum() != null && baseQueryVO.getPageSize() != null) {
                PageHelper.startPage(baseQueryVO.getPageNum(), baseQueryVO.getPageSize());
            }

            // 判断排序参数是否存在
            String orderColumn = baseQueryVO.getOrderColumn();
            if (orderColumn != null) {
                // 如果存在，则进行类型校验，判断排序传入的字符串是否存在于传入的VO中，作为sql防注入手段

                // 获取实际类型
                Class<?> clazz = baseQueryVO.getClass();
                Field field;
                if (!isFieldPresent(orderColumn, clazz)) {
                    throw new BaseException("排序字段不存在！");
                }
                baseQueryVO.setOrderColumn(orderColumn);

                // 默认升序
                String sort = String.valueOf(OrderEnums.ASC);
                if (baseQueryVO.getOrderSort() != null) {
                    sort = baseQueryVO.getOrderSort();
                }
                PageHelper.orderBy(camelToUnderscore(baseQueryVO.getOrderColumn()) + " " + sort);
            }
        }
    }

    /**
     * 类中属性转数据库字段
     *
     * @param field 字段名
     * @return java.lang.String
     * @author Idenn
     * @date 3/12/2024 2:45 PM
     */
    public static String camelToUnderscore(String field) {
        return field.replaceAll("([A-Z])", "_" + "$0").toLowerCase();
    }

    /**
     * 递归判断字段是否存在于类对象，以及它父类对象中
     *
     * @param fieldName 字段名
     * @param type 判断的类对象
     * @return boolean
     * @author Idenn
     * @date 3/12/2024 2:46 PM
     */
    public static boolean isFieldPresent(String fieldName, Class<?> type) {
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            // 跳过基础查询VO
            if (c.equals(BaseQueryVO.class)) continue;
            try {
                c.getDeclaredField(fieldName);
                return true;
            } catch (NoSuchFieldException e) {
                // Field not found in this class, continue with the next superclass
            }
        }
        return false;
    }
}
