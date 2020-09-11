package sql.hibernate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @author linjing
 * @date: Created in 2020/9/11
 *      * create table student(
 *      * id varchar2(50),
 *      * name varchar2(50),
 *      * age  number(3)
 *      * );
 */
@Entity
public class StudentXml {
    private String id;
    private String name;
    private Byte age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getAge() {
        return age;
    }

    public void setAge(Byte age) {
        this.age = age;
    }
}
