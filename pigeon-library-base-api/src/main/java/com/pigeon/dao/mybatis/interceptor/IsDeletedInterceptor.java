package com.pigeon.dao.mybatis.interceptor;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 逻辑删除判断插件
 *
 * @author Idenn
 * @date 3/12/2024 4:13 PM
 */
public class IsDeletedInterceptor extends JsqlParserSupport implements InnerInterceptor {

    /*
     * 操作sql前置处理逻辑
     */
    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);

        MappedStatement ms = mpSh.mappedStatement();
        SqlCommandType sct = ms.getSqlCommandType();
        if (sct == SqlCommandType.UPDATE || sct == SqlCommandType.SELECT) {
            // 调用 JSqlParser工具修改sql后执行
            PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
            mpBs.sql(parserMulti(mpBs.sql(), null));
        }
    }

    /*
     * select时调用
     */
    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        // 如果是查询操作
        if (select.getSelectBody() instanceof PlainSelect) {
            reformatPlainSelect((PlainSelect) select.getSelectBody());
        }
    }

    /**
     * 格式化查询语句
     *
     * @param plainSelect 查询语句
     * @return void
     * @author Idenn
     * @date 3/12/2024 4:56 PM
     */
    private void reformatPlainSelect(PlainSelect plainSelect) {

        //获取最外层表名
        Table outTable = (Table) plainSelect.getFromItem();
        // 等于表达式
        EqualsTo outEqualsTo = new EqualsTo();
        outEqualsTo.setLeftExpression(new Column(outTable, "is_deleted"));
        outEqualsTo.setRightExpression(new StringValue("0"));

        // 判断where条件
        Expression outExpression = plainSelect.getWhere();
        if (outExpression != null) {
            // 判断之前有没有is_delete
            if (!checkIsDeletedCondition(outExpression)) {
                AndExpression andExpression = new AndExpression();
                andExpression.setLeftExpression(outExpression);
                andExpression.setRightExpression(outEqualsTo);
                plainSelect.setWhere(andExpression);
            }
        } else {
            plainSelect.setWhere(outEqualsTo);
        }

        // 对join进行操作
        if (plainSelect.getJoins() != null) {
            processJoins(plainSelect.getJoins());
        }
    }

    /**
     * 格式化查询语句中的join
     *
     * @param joins join条件
     * @return void
     * @author Idenn
     * @date 3/12/2024 4:56 PM
     */
    private void processJoins(List<Join> joins) {
        for (Join join : joins) {
            // 获取表名
            Table inTable = (Table) join.getRightItem();

            // 等于表达式
            EqualsTo inEqualsTo = new EqualsTo();
            inEqualsTo.setLeftExpression(new Column(inTable, "is_deleted"));
            inEqualsTo.setRightExpression(new StringValue("0"));

            // 初始化
            List<Expression> expressions = new ArrayList<>();

            // 设置
            Collection<Expression> onExpressions = join.getOnExpressions();
            Expression inExpression = join.getOnExpressions().size() > 0 ? (Expression) onExpressions.toArray()[0] : null;
            if (inExpression != null) {
                // 判断之前有没有is_delete
                if (!checkIsDeletedCondition(inExpression)) {
                    AndExpression andExpression = new AndExpression();
                    andExpression.setLeftExpression(inExpression);
                    andExpression.setRightExpression(inEqualsTo);
                    expressions.add(andExpression);
                    join.setOnExpressions(expressions);
                }
            } else {
                expressions.add(inEqualsTo);
                join.setOnExpressions(expressions);
            }

        }
    }

    /*
     * update时调用
     */
    @Override
    protected void processUpdate(Update update, int index, String sql, Object obj) {

        //获取表名
        Table table = (Table) update.getFromItem();

        // 等于表达式
        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(new Column(table, "is_deleted"));
        equalsTo.setRightExpression(new StringValue("0"));

        // 判断where条件
        Expression expression = update.getWhere();
        if (expression != null) {
            // 判断之前有没有is_delete
            if (!checkIsDeletedCondition(expression)) {
                AndExpression andExpression = new AndExpression();
                andExpression.setLeftExpression(expression);
                andExpression.setRightExpression(equalsTo);
                update.setWhere(andExpression);
            }
        } else {
            update.setWhere(equalsTo);
        }
    }

    /**
     * 检查 "is_deleted = 0" 这个条件是否存在于 where 子句中
     *
     * @param where where条件
     * @return boolean
     * @author Idenn
     * @date 3/12/2024 5:06 PM
     */
    private boolean checkIsDeletedCondition(Expression where) {
        return where.toString().contains("is_deleted");
    }
}
