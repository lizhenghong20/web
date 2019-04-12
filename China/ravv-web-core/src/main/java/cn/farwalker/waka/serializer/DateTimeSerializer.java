package cn.farwalker.waka.serializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateTimeSerializer extends JsonSerializer<Date> {

	//  用于储存日期串行化格式
    private final String key;
    
    /**
     * @param key
     */
    public DateTimeSerializer(String key) {
        super();
        this.key = key;
    }
    
	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
		throws IOException, JsonProcessingException {
        String output = StringUtils.EMPTY;
        if(value != null) {
            output = new SimpleDateFormat(key).format(value);
        }
        gen.writeString(output);
    }
}
