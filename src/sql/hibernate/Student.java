package sql.hibernate;

import javax.persistence.*;
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
@Table(name="student",schema = "app_taat")
public class Student {
    private String id;
    private String name;
    private Byte age;

    
    @Id
    @Column(name = "ID",  nullable = true, length = 50)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "NAME",  nullable = true, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    @Column(name = "AGE",  nullable = true, precision = 0)
    public Byte getAge() {
        return age;
    }

    public void setAge(Byte age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) &&
                Objects.equals(name, student.name) &&
                Objects.equals(age, student.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age);
    }
}
