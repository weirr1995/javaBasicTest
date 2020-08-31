package dao.jdbc;

import com.alibaba.druid.util.StringUtils;
import io.netty.util.internal.StringUtil;
import org.junit.Test;

import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author linjing
 * @date: Created in 2020/8/27
 */
public class TestSql {
    @Test
    public void test(){
        String sql = "select 1 from dual";
        Map params = new HashMap();
        params.put("cdate","20200827");
        System.out.println(getFullSql(sql,params));
    }

    private String getFullSql(String sql,Map params){
        StringBuilder sqlB =  new StringBuilder();
        sqlB.append("SELECT t.* FROM ( ");
        sqlB.append(sql+" ");
        sqlB.append(") t ");
        if (params.size() >= 1) {
            sqlB.append("WHERE 1=1  ");
            Iterator it = params.keySet().iterator();
            while (it.hasNext()) {
                String key = String.valueOf(it.next());
                String value = String.valueOf(params.get(key));
                if (!StringUtils.isEmpty(value) && !"null".equals(value)) {
                    sqlB.append(" AND ");
                    sqlB.append(" " + key+" = '"+value+"'" );
                }
            }
        }
        return sqlB.toString();
    }
}
