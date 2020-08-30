package sql.spring.jdbctemplate;
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
import java.sql.SQLException;

/**
 * @author linjing
 * @date: Created in 2020/6/30
 * 有普通返回值的存储过程调用
 */
/*create table MyTable(
        id varchar2(255),
        name varchar2(255)
        );*/
/*create or replace procedure sp_select_table (param1 in varchar2,param2 out varchar2) as
 begin select name into param2 from MyTable where ID = param1 ;
end sp_select_table ; */
@RunWith(SpringJUnit4ClassRunner.class)//用来加载spring配置
@ContextConfiguration("classpath:applicationContext.xml")//classpath:表示从根路径查找xml文件
public class JdbcTemplateProcedureTest02 {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void test() {
        String param2Value = (String) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call sp_select_table (?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, "param1");// 设置输入参数的值
                        cs.registerOutParameter(2,OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        return cs.getString(2);// 获取输出参数的值
                    }
                });
    }
}
