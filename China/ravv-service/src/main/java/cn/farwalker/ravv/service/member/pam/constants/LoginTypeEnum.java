package cn.farwalker.ravv.service.member.pam.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsonn;
import cn.farwalker.waka.orm.EnumManager.IEnumJsons;



/**
 * Created by asus on 2018/11/10.
 */
public enum LoginTypeEnum implements IEnumJsonn{
        EMAIL(1,"email"),
        WECHAT(2,"wechat"),
        PHONE(3,"phone");
       
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
}
