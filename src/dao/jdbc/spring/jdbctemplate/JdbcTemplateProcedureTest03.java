package dao.jdbc.spring.jdbctemplate;
import oracle.jdbc.OracleTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linjing
 * @date: Created in 2020/6/30
 * 有游标返回值的存储过程调用
 */
/*create table MyTable(
        id varchar2(255),
        name varchar2(255)
        );*/
/*create or replace package mypackage as
    type my_cursor is ref cursor;
    end mypackage;*/
/*create or replace procedure sp_list_table(param1 in varchar2,param2 out mypackage.my_cursor) is
        begin
open my_cursor for select * from myTable;
        end sp_list_table;*/
@RunWith(SpringJUnit4ClassRunner.class)//用来加载spring配置
@ContextConfiguration("classpath:applicationContext.xml")//classpath:表示从根路径查找xml文件
public class JdbcTemplateProcedureTest03 {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void test() {
        List resultList = (List) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call sp_list_table(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, "p1");// 设置输入参数的值
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List resultsMap = new ArrayList();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        while (rs.next()) {// 转换每行的返回值到Map中
                            Map rowMap = new HashMap();
                            rowMap.put("id", rs.getString("id"));
                            rowMap.put("name", rs.getString("name"));
                            resultsMap.add(rowMap);
                        }
                        rs.close();
                        return resultsMap;
                    }
                });
        for (int i = 0; i < resultList.size(); i++) {
            Map rowMap = (Map) resultList.get(i);
            String id = rowMap.get("id").toString();
            String name = rowMap.get("name").toString();
            System.out.println("id=" + id + ";name=" + name);
        }
    }
}
