package dao.jdbc.spring.jdbctemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author linjing
 * @date: Created in 2020/6/30
 * 无返回值的存储过程调用
 */
/*create table MyTable(
        id varchar2(255),
        name varchar2(255)
        );*/
/*create or replace procedure sp_insert_table(param1 in varchar2,param2 in varchar2) as
        begin
        insert into  MyTable (id,name) values ('param1 ','param2');
        end sp_insert_table;*/
@RunWith(SpringJUnit4ClassRunner.class)//用来加载spring配置
@ContextConfiguration("classpath:applicationContext.xml")//classpath:表示从根路径查找xml文件
public class JdbcTemplateProcedureTest01 {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void test(){
        this.jdbcTemplate.execute("call sp_insert_table('100001','linjing')");
    }
}
