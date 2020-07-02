package dao.jdbc.spring.jdbctemplate;

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
