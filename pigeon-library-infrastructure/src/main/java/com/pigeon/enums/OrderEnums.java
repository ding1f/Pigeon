package com.pigeon.enums;

/**
 * 查询结果排序用常量值
 *
 * @author Idenn
 * @date 3/11/2024 8:21 PM
 */
public enum OrderEnums {
    ASC, DESC;

    public static boolean contains(String test) {
        for (OrderEnums c : OrderEnums.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }
        return false;
    }
}
