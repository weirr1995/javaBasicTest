package date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author linjing
 * @date: Created in 2020/6/30
 */
public class FindDates {
    public static List<String> findDates(String stime, String etime)
            throws ParseException {
        List<String> allDate = new ArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dBegin = sdf.parse(stime);
        Date dEnd = sdf.parse(etime);
        allDate.add(sdf.format(dBegin));
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            allDate.add(sdf.format(calBegin.getTime()));
        }
        return allDate;
    }
    public static void main(String[] args) {
        //测试数据
        String stime = "2019-05-01";
        String etime = "2019-05-05";
        //集合中包含2019-05-01/2019-05-05，不需要可去除
        List<String> list = new ArrayList<String>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            long s = Long.valueOf(sdf.parse(stime).getTime());
            long e = Long.valueOf(sdf.parse(etime).getTime());
            //只有结束时间大于开始时间时才进行查询
            if(s<e) {
                list = findDates(stime, etime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(String time : list) {
            System.out.println(time);
        }
        System.out.println("间隔天数：" + list.size());
    }
}
