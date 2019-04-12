package cn.farwalker.standard.util;

import cn.farwalker.waka.util.Tools;
import com.alibaba.fastjson.JSON;

import cn.farwalker.waka.auth.converter.BaseTransferEntity;
import cn.farwalker.waka.auth.security.impl.Base64SecurityAction;

public class JwtUtil {
	
	public static BaseTransferEntity wrapAsTransferEntity(Object obj, String randomKey) {
		String jsonString = JSON.toJSONString(obj);
        String encode = new Base64SecurityAction().doAction(jsonString);
        String md5 = Tools.md5.encrypt(encode + randomKey);

        BaseTransferEntity baseTransferEntity = new BaseTransferEntity();
        baseTransferEntity.setObject(encode);
        baseTransferEntity.setSign(md5);
        
        return baseTransferEntity;
	}

}
