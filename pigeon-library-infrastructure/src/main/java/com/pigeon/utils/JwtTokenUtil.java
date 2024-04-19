package com.pigeon.utils;

import com.pigeon.entity.pojo.SecurityContextPOJO;
import com.pigeon.enums.ResultCode;
import com.pigeon.exception.BaseException;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 解析jwt工具类
 *
 * @author Idenn
 * @date 3/25/2024 4:25 PM
 */
@Component
public class JwtTokenUtil {

    @Resource
    private SecurityContextPOJO securityContextPOJO;

    public Claims parseJwt(String token) {
        try {
            return Jwts.parser().setSigningKey(securityContextPOJO.getSigningKey().getBytes()).parseClaimsJws(token).getBody();
        } catch (MalformedJwtException e) {
            // 捕获json解析失败异常
            throw new BaseException(ResultCode.UNAUTHORIZED.getCode(), "token解析失败！");
        }
        catch (SignatureException e) {
            // 捕获签名异常的错误
            throw new BaseException(ResultCode.UNAUTHORIZED.getCode(), "签名被篡改！");
        }  catch (ExpiredJwtException e) {
            // 这里捕获的是JWT中的签发时间是否过期，但是这个框架采用的是redis校验token是否存在来验证是否过期（因为有续签功能）
            // 所以如果JWT过期，从ExpiredJwtException中获取Claims
            return e.getClaims();
        }
    }

    public String getUserInfoFrom(String token) {
        Claims claims = parseJwt(token);
        return claims.get("user_name", String.class); // 获取user_name信息
    }

    public List<String> getAuthoritiesFromToken(String token) {
        Claims claims = parseJwt(token);
        return claims.get("authorities", List.class); // 获取authorities信息
    }
}