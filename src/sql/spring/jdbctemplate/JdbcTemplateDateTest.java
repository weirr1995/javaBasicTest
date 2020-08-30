package sql.spring.jdbctemplate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author linjing
 * @date: Created in 2020/7/1
 * @Description: 插入日期类型的值
 */
/*create table MyTable(
        id varchar2(255),
        name varchar2(255),
        birthday date
        );*/
@RunWith(SpringJUnit4ClassRunner.class)//用来加载spring配置
@ContextConfiguration("classpath:applicationContext.xml")//classpath:表示从根路径查找xml文件
public class JdbcTemplateDateTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void test(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
           // Date birthday = simpleDateFormat.parse("19901001");
            Date birthday = null;
            String sql = "insert into MyTable(id,name,birthday) values (?,?,?)";
            this.jdbcTemplate.update(sql,"1","linjing",birthday);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
