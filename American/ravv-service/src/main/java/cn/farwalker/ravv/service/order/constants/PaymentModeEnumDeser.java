package cn.farwalker.ravv.service.order.constants;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;

public class PaymentModeEnumDeser implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
        JSONObject object = defaultJSONParser.parseObject();
        String key = object.getString("key");
        return (T)PaymentModeEnum.value(key);
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
