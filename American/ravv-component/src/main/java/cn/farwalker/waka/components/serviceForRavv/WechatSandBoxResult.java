package cn.farwalker.waka.components.serviceForRavv;

import lombok.Data;

@Data
public class WechatSandBoxResult {
    private String return_code;
    private String return_msg;
    private String mch_id;
    private String sandbox_signkey;
}
