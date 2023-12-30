package P08Exams.P04Exam;

import P08Exams.P04Exam.core.Expressionist;
import P08Exams.P04Exam.core.ExpressionistImpl;
import P08Exams.P04Exam.models.Expression;
import P08Exams.P04Exam.models.ExpressionType;

public class Main {
    public static void main(String[] args) {
        Expressionist expressionist = new ExpressionistImpl();

        expressionist.addExpression(new Expression() {{
            setId("1");
            setType(ExpressionType.OPERATOR);
            setValue("+");
        }});
        expressionist.addExpression(new Expression() {{
            setId("2");
            setType(ExpressionType.OPERATOR);
            setValue("*");
        }}, "1");
        expressionist.addExpression(new Expression() {{
            setId("3");
            setType(ExpressionType.OPERATOR);
            setValue("/");
        }}, "1");
        expressionist.addExpression(new Expression() {{
            setId("4");
            setType(ExpressionType.VALUE);
            setValue("5");
        }}, "2");
        expressionist.addExpression(new Expression() {{
            setId("5");
            setType(ExpressionType.VALUE);
            setValue("10");
        }}, "2");
        expressionist.addExpression(new Expression() {{
            setId("6");
            setType(ExpressionType.VALUE);
            setValue("2.5");
        }}, "3");
        expressionist.addExpression(new Expression() {{
            setId("7");
            setType(ExpressionType.VALUE);
            setValue("3.5");
        }}, "3");

        System.out.println(expressionist.evaluate());
    }
}