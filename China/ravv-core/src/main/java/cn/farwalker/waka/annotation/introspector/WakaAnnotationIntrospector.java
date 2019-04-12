package cn.farwalker.waka.annotation.introspector;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import cn.farwalker.waka.annotation.DateFormatter;
import cn.farwalker.waka.serializer.DateTimeSerializer;

public class WakaAnnotationIntrospector extends JacksonAnnotationIntrospector {
	/**
	 * 
	 */
	private static final long serialVersionUID = 751180282461589733L;

	@Override
    public Object findSerializer(Annotated annotated) {
        //  经测试,只对方法有用
        if(annotated instanceof AnnotatedMethod) {
            DateFormatter formatter = annotated.getAnnotated().getAnnotation(DateFormatter.class);
            if(formatter != null) {
                return new DateTimeSerializer(formatter.value());
            }
        }
        return super.findSerializer(annotated);
    }
}
