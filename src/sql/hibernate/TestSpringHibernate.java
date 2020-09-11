package sql.hibernate;

import com.sun.javaws.Globals;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * @author linjing
 * @date: Created in 2020/9/11
 */
public class TestSpringHibernate {

    @Test
    public void test(){
        ApplicationContext ioc=new ClassPathXmlApplicationContext("applicationContext.xml");
        DataSource da=ioc.getBean(DataSource.class);
        HibernateTemplate ht=ioc.getBean(HibernateTemplate.class);
        try {
            System.out.println(da.getConnection());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void test2(){
        ApplicationContext ioc=new FileSystemXmlApplicationContext(System.getProperty("user.dir")+"\\resources\\applicationContext.xml");
        HibernateTemplate ht=ioc.getBean("hibernateTemplate",HibernateTemplate.class);
        Map params = new HashMap();
        List<Student> students =  find(Student.class,params,ht);
        for (Student student:students) {
            System.out.println(student.getName());
        }
    }


    public <T> List<T> find(Class<T> clazz, Map<String, Object> params,HibernateTemplate ht) {
        List<Object> list = new ArrayList();
        String hql = " select o from " + clazz.getName() + " o where 1=1 ";
        Iterator it = params.entrySet().iterator();

        while(it.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry)it.next();
            String key = (String)entry.getKey();
            Object value = entry.getValue();
            if (value != null) {
                String op = " = ";
                String inner;
                int index;
                if ((index = key.indexOf(40)) >= 0) {
                    inner = key.substring(index);
                    if ("like".equals(inner)) {
                        op = " like ";
                        value = value + "%";
                    }

                    key = key.substring(0, index);
                } else if ((index = key.indexOf(35)) >= 0) {
                    inner = key.substring(index + 1);
                    if ("like".equals(inner)) {
                        op = " like ";
                        value = value + "%";
                    } else if ("lrlike".equals(inner)) {
                        op = " like ";
                        value = "%" + value + "%";
                    } else if ("llike".equals(inner)) {
                        op = " like ";
                        value = "%" + value;
                    }

                    key = key.substring(0, index);
                }

                hql = hql + " and o." + key + op + "?";
                list.add(value);
            }
        }
        return (List<T>) ht.find(hql, list.toArray());
    }
}
