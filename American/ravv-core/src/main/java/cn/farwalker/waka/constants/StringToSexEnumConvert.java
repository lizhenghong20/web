package cn.farwalker.waka.constants;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToSexEnumConvert implements Converter<String, SexEnum> {
    @Override
    public SexEnum convert(String s) {

        return SexEnum.getEnumByKey(s);
    }

    public static <T extends SexEnum> Object getEnum(Class<T> targerType, String source){
        for(T enumObj : targerType.getEnumConstants()){
            if(source.equals(enumObj.getKey()))
                return enumObj;
        }
        return null;
    }
}
