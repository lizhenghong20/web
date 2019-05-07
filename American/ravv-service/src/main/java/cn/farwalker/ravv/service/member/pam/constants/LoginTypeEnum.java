package cn.farwalker.ravv.service.member.pam.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsonn;
import cn.farwalker.waka.orm.EnumManager.IEnumJsons;
import com.alibaba.fastjson.annotation.JSONType;


/**
 * Created by asus on 2018/11/10.
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = LoginTypeEnumDeser.class)
public enum LoginTypeEnum implements IEnumJsonn{
        EMAIL(1,"EMAIL"),
        FACEBOOK(2,"FACEBOOK"),
        GOOGLE(3,"GOOGLE");
       
    private final Integer key;
    private final String label;
    LoginTypeEnum(Integer key, String label) {
        this.key = key;
        this.label = label;
    }


	@Override
	public String getLabel() { 
		return label;
	}

	@Override
	public Integer getKey() { 
		return key;
	}

	public static LoginTypeEnum value(String label){
        if("FACEBOOK".equals(label)){
            return FACEBOOK;
        } else if("GOOGLE".equals(label)){
            return GOOGLE;
        }
        return null;
    }
}
