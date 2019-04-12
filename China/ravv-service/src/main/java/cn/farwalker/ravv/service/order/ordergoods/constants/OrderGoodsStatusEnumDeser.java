package cn.farwalker.ravv.service.order.ordergoods.constants;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;

public class OrderGoodsStatusEnumDeser implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
        JSONObject jsonObject = defaultJSONParser.parseObject();
        String key = jsonObject.getString("key");
        return (T)OrderGoodsStatusEnum.value(key);
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
