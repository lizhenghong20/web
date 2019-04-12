package cn.farwalker.ravv.admin.login.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 认证的响应结果
 *
 * @author Jason Chen
 * @Date 2018/02/13 14:00
 */
@Data
public class AuthResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    /**
     * 用于前端判断返回正确
     */
    private final int code = 0;
    
    private String account;
    
    /**
     * 会员id，用于前端存储
     */
    private Long memberId;
    /**
     * jwt token
     */
    private final String token;

    /**
     * 用于客户端混淆md5加密
     */
    private final String randomKey;

    public AuthResponse(String token, String randomKey) {
        this.token = token;
        this.randomKey = randomKey;
    }


}
