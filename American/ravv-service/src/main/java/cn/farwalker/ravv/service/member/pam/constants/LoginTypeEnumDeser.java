package cn.farwalker.ravv.service.member.pam.constants;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;

public class LoginTypeEnumDeser implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
        JSONObject object = defaultJSONParser.parseObject();
        String lable = object.getString("lable");
        return (T)LoginTypeEnum.value(lable);
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
