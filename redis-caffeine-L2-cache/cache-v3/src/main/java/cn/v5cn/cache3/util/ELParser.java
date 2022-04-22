package cn.v5cn.cache3.util;

import cn.v5cn.cache.base.entity.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.TreeMap;

/**
 * @author ZYW
 * @version 1.0
 * @date 2022/4/11 9:49 下午
 */
public class ELParser {
    public static String parse(String elString, TreeMap<String, Object> map) {
        elString = String.format("#{%s}", elString);
        //创建表达式解析器
        ExpressionParser parser = new SpelExpressionParser();
        //通过evaluationContext.setVariable可以在上下文中设定变量。
        EvaluationContext context = new StandardEvaluationContext();
        map.entrySet().forEach(entry -> {
            context.setVariable(entry.getKey(), entry.getValue());
        });

        //解析表达式，如果表达式是一个模板表达式，需要为解析传入模板解析器上下文
        Expression expression = parser.parseExpression(elString, new TemplateParserContext());
        //使用Expression.getValue()获取表达式的值，这里传入了Evaluation上下文
        String value = expression.getValue(context, String.class);
        return value;
    }

    public void test() {
        String elString="#order.money";
        String elString2="#user";
        String elString3="#p0";   //暂不支持

        TreeMap<String,Object> map=new TreeMap<>();
        Order order = new Order();
        order.setId(111L);
        order.setMoney(123D);
        map.put("order",order);
        map.put("user","Hydra");

        String val = parse(elString, map);
        String val2 = parse(elString2, map);
        String val3 = parse(elString3, map);

        System.out.println(val);
        System.out.println(val2);
        System.out.println(val3);
    }
}
