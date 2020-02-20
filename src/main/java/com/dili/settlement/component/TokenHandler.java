package com.dili.settlement.component;

import com.dili.settlement.util.GeneralUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 用于简单验证结算过程数据是否被改
 */
@Component
public class TokenHandler {

    @Value("${settle.token.key:asdf@1234}")
    private String key;

    /**
     * 返回签名
     * @param content
     * @return
     */
    public String generate(String content) {
        StringBuilder builder = new StringBuilder();
        builder.append(key).reverse().append(content).append(key);
        return GeneralUtil.md5(builder.toString());
    }

    /**
     * 验证签名
     * @param content
     * @param token
     * @return
     */
    public boolean valid(String content, String token) {
        return generate(content).equals(token);
    }
}
