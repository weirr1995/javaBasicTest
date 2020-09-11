package sql.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author linjing
 * @date: Created in 2020/9/10
 * 1.Hibernate基本步骤
 * 1.1.引入jar包
 * 1.2.编写hibernate.cfg.xml文件
 * 1.3.创建持久化类，实现Serializable接口
 * 1.4.为持久化类编写.hbm.xml映射文件
 * 1.5.中节点引用.hbm.xml文件
 * 1.6.Java代码运行
 */
public class TestJavaHibernate {

    @Test
    public void test1(){
        System.out.println(System.getProperty("user.dir"));
    }

    @Test
    public void test(){
        Configuration cfg=new Configuration();
        SessionFactory sf=cfg.configure().buildSessionFactory();
        Session session=sf.openSession();
        session.beginTransaction();
        StudentXml student = new StudentXml();
        student.setId("1");
        student.setName("linjing");
        session.save(student);
        session.getTransaction().commit();
    }

    @Test
    public void testAnotation(){
        Configuration cfg=new Configuration();
        SessionFactory sf=cfg.configure().buildSessionFactory();
        Session session=sf.openSession();
        session.beginTransaction();
        Student student = new Student();
        student.setId("1");
        student.setName("linjing");
        session.save(student);
        session.getTransaction().commit();
    }

}
