package com.pigeon.utils;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 将包含中文的信息转换为UTF-8编码的字节流
 *
 * @author Idenn
 * @date 3/30/2024 4:08 PM
 */
@Component
public class FeignBase64Util {

    /*
     * 编码
     */
    public String base64Encoder(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    /*
     * 解码
     */
    public String base64Decoder(String value) {
        byte[] decodedBytes = Base64.getDecoder().decode(value);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
