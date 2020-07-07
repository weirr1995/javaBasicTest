package date;

import com.alibaba.druid.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.util.List;

/**
 * @author linjing
 * @date: Created in 2020/7/6
 */
@RunWith(SpringJUnit4ClassRunner.class)//用来加载spring配置
@ContextConfiguration("classpath:applicationContext.xml")//classpath:表示从根路径查找xml文件
@Slf4j
public class CalWorkDays {
    private static final String STR_SCHEMA = "gjdfppos.";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void calNvalue(){
        int nValue =0;
        //测试数据
        String strBeginDate = "20200706";
        String strEndDate = "20200707";
        if(StringUtils.isEmpty(strBeginDate) || StringUtils.isEmpty(strEndDate)){
            log.info("N值为：{}",nValue);
            return;
        }

        List<String> dates;
        int count =0;
        try {
            dates = FindDates.findDates(strBeginDate, strEndDate);
            if(dates == null || dates.size() <=0){
                log.info("N值为：{}",nValue);
                return ;
            }
            for (String theDay :dates) {
                if(isTaWorkDay(theDay)){
                    count++;
                    continue;
                }
            }
            nValue = count;
        } catch (ParseException e) {
            e.printStackTrace();
            log.info("N值为：{}",nValue);
            return ;
        }
        log.info("N值为：{}",nValue);
        return ;
    }

    /**
     * 是否TA工作日
     * @param today
     * @return
     */
    public   boolean isTaWorkDay(String today) {
        boolean flag = false;
        String sql = "select  to_char(t.L_WORKFLAG) workflag  from  "+ STR_SCHEMA +"v_ta_topenday t where t.D_DATE = ?";
        Object[] objects = new Object[]{today};
        String workflag = jdbcTemplate.queryForObject(sql,objects,String.class);
        if(workflag != null  &&  "1".equals(workflag)){
            return true;
        }
        return flag;
    }
}
