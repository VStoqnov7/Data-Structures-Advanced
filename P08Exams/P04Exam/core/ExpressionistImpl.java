package P08Exams.P04Exam.core;

import P08Exams.P04Exam.models.Expression;
import P08Exams.P04Exam.models.ExpressionType;

import java.util.*;

public class ExpressionistImpl implements Expressionist {

    private Expression root;
    private Map<String, Expression> expressions;
    private Map<String, Set<String>> parentChildMap;

    public ExpressionistImpl() {
        this.root = null;
        this.expressions = new HashMap<>();
        this.parentChildMap = new HashMap<>();
    }


    @Override
    public void addExpression(Expression expression) {
        if (root == null) {
            root = expression;
            expressions.put(expression.getId(), expression);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void addExpression(Expression expression, String parentId) {
        Expression parent = expressions.get(parentId);
        if (parent == null) {
            throw new IllegalArgumentException();
        }

        if (parent.getLeftChild() == null) {
            parent.setLeftChild(expression);
        } else if (parent.getRightChild() == null) {
            parent.setRightChild(expression);
        } else {
            throw new IllegalArgumentException();
        }
        expressions.put(expression.getId(), expression);
        Set<String> children = parentChildMap.computeIfAbsent(parentId, k -> new HashSet<>());
        children.add(expression.getId());
    }

    @Override
    public boolean contains(Expression expression) {
        return expressions.containsKey(expression.getId());
    }

    @Override
    public int size() {
        return expressions.size();
    }

    @Override
    public Expression getExpression(String expressionId) {
        Expression expression = expressions.get(expressionId);
        if (expression == null) {
            throw new IllegalArgumentException();
        }

        return expression;
    }

    @Override
    public void removeExpression(String expressionId) {
        Expression result = this.expressions.get(expressionId);

        if (result == null) {
            throw new IllegalArgumentException();
        }
        for (Expression value : this.expressions.values()) {
            Expression root = value;
            boolean flag = false;
            if (value.getLeftChild() == result) {
                removeExpr(result);
                root.setLeftChild(root.getRightChild());
                root.setRightChild(null);
                flag = true;
            } else if (value.getRightChild() == result) {
                removeExpr(result);
                root.setRightChild(null);
                flag = true;
            }
            if (flag) {
                return;
            }
        }
    }

    public void removeExpr (Expression expression) {
        if (expression.getLeftChild() != null) {
            this.removeExpr(expression.getLeftChild());
            expression.setLeftChild(null);
        }

        if (expression.getRightChild() != null) {
            this.removeExpr(expression.getRightChild());
            expression.setRightChild(null);
        }
        this.expressions.remove(expression.getId());
    }

    @Override
    public String evaluate() {
        if (root == null) {
            return "";
        }
        return evaluateExpression(root).toString();
    }

    private StringBuilder evaluateExpression(Expression expression) {
        StringBuilder sb = new StringBuilder();

        if (expression.getType().equals(ExpressionType.VALUE)) {
            sb.append(expression.getValue());
        } else if (expression.getType().equals(ExpressionType.OPERATOR)) {
            StringBuilder left = evaluateExpression(expression.getLeftChild());
            StringBuilder right = evaluateExpression(expression.getRightChild());
            sb.append("(").append(left).append(" ").append(expression.getValue()).append(" ").append(right).append(")");
        }
        return sb;
    }
}
