package com.pigeon.context;

import com.pigeon.entity.pojo.UserPOJO;

/**
 * 持有用户登录信息
 *
 * @author Idenn
 * @date 3/25/2024 3:14 PM
 */
public class UserHolder {
    private static final ThreadLocal<UserPOJO> userThreadLocal = new ThreadLocal<>();

    public static void setUser(UserPOJO userPOJO) {
        userThreadLocal.set(userPOJO);
    }

    public static UserPOJO getUser() {
        return userThreadLocal.get();
    }

    public static void clear() {
        userThreadLocal.remove();
    }
}
