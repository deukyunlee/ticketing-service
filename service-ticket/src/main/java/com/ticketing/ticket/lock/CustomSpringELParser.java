package com.ticketing.ticket.lock;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.concurrent.ConcurrentHashMap;

public final class CustomSpringELParser {

    private static final ExpressionParser PARSER = new SpelExpressionParser();
    private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
    /** 분산 락 키용 SpEL은 패턴 종류가 적어 파싱 결과를 재사용한다. */
    private static final ConcurrentHashMap<String, Expression> PARSED_EXPRESSIONS = new ConcurrentHashMap<>();

    private CustomSpringELParser() {
    }

    public static String getDynamicValue(Object[] args, java.lang.reflect.Method method, String expression) {
        String[] parameterNames = NAME_DISCOVERER.getParameterNames(method);
        StandardEvaluationContext context = new StandardEvaluationContext();

        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }
        }
        Expression parsed = PARSED_EXPRESSIONS.computeIfAbsent(expression, PARSER::parseExpression);
        return parsed.getValue(context, String.class);
    }
}
