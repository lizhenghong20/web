package cn.farwalker.ravv.service.email;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class MyCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return "true".equalsIgnoreCase(conditionContext.getEnvironment().getProperty("conditional.load"));
    }
}
