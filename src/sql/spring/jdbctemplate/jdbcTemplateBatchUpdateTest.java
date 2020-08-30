package sql.spring.jdbctemplate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author linjing
 * @date: Created in 2020/7/1
 * 批量更新测试
 * 需注意大数据量批量更新，如果没有设置参数为true，或者没有指定参数类型，会很慢。
 */
/**
 * 如果数据库连接参数中不添加rewriteBatchedStatements 参数,MySQL Jdbc驱动在默认情况下会无视executeBatch()语句，截图最下边那行源码 this.executeBatchSerially(batchTimeout) 会一条一条的执行，只有把rewriteBatchedStatements参数置为true, 驱动才会帮你批量执行。
 * ————————————————
 * 版权声明：本文为CSDN博主「_itors」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/qq_23078761/article/details/86505596
 */

/**
 * 就我而言，使用Spring 4.1.4和Oracle 12c，插入包含35个字段的5000行：
 *
 * jdbcTemplate.batchUpdate(insert, parameters); // Take 7 seconds
 *
 * jdbcTemplate.batchUpdate(insert, parameters, argTypes); // Take 0.08 seconds!!!
 * argTypes参数是一个int数组，您可以用这种方式设置每个字段：
 *
 * int[] argTypes = new int[35];
 * argTypes[0] = Types.VARCHAR;
 * argTypes[1] = Types.VARCHAR;
 * argTypes[2] = Types.VARCHAR;
 * argTypes[3] = Types.DECIMAL;
 * argTypes[4] = Types.TIMESTAMP;
 * .....
 * 我调试了org \ springframework \ jdbc \ core \ JdbcTemplate.java，发现大部分时间都在消耗，试图了解每个字段的性质，这是为每条记录做的。
 */
/*create table MyTable(
        id varchar2(255),
        name varchar2(255),
        birthday date
        );*/
@RunWith(SpringJUnit4ClassRunner.class)//用来加载spring配置
@ContextConfiguration("classpath:applicationContext.xml")//classpath:表示从根路径查找xml文件
public class jdbcTemplateBatchUpdateTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void test(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String paramKeyList = "id,name,birthday";
        List batUpdateList = new ArrayList();
        List<Map<String,Object>> personMapList = new ArrayList<Map<String, Object>>();

        for(int i=0;i<5;i++){
            Map person = new HashMap();
            person.put("id",i);
            person.put("name","name"+i);
            person.put("birthday",new Date());
            personMapList.add(person);
        }
        if(personMapList != null && personMapList.size()>0){
            for (Map<String,Object> openPeriod:personMapList ) {
                List paramList = new ArrayList();
                for (String key:paramKeyList.split(",")) {
                    paramList.add(openPeriod.get(key));
                }
                Object[] objects =paramList.toArray();//重点语句
                batUpdateList.add(objects);
            }
        }

        try {
            Date birthday = null;
            String sql = "insert into MyTable(id,name,birthday) values (?,?,?)";
            this.jdbcTemplate.batchUpdate(sql,batUpdateList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
