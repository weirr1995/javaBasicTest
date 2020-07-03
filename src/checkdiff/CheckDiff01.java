package checkdiff;

import com.alibaba.fastjson.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author linjing
 * @date: Created in 2020/7/2
 * A，B比对功能，实现如：一天对一天，一天对一段，一段对另一段，一段对另多段比较。
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)//用来加载spring配置
@ContextConfiguration("classpath:applicationContext.xml")//classpath:表示从根路径查找xml文件
public class CheckDiff01 {
    private static final String CHECKRESULT_NOTOK = "0";
    private static final String CHECKRESULT_OK = "1";
    private static final String SOURCE_FLAG_A = "A";
    private static final String SOURCE_FLAG_B = "B";
    private static final String CHECKFAILREASON_A = "A方独有";
    private static final String CHECKFAILREASON_B = "B方独有";

    @Test
    public void test(){
        String sourceObjectJsonStr ="[{C_BEGINDATE:'20200501',C_ENDDATE:'20200505',C_OPENSTATUS:'1'}" +
                "                ,{C_BEGINDATE:'20200506',C_ENDDATE:'20200506',C_OPENSTATUS:'1'}"+
                "                ,{C_BEGINDATE:'20200507',C_ENDDATE:'20200515',C_OPENSTATUS:'1'}"+
                "                ,{C_BEGINDATE:'20200514',C_ENDDATE:'20200515',C_OPENSTATUS:'1'}]";

        String targetObjectJsonStr ="[{C_BEGINDATE:'20200501',C_ENDDATE:'20200516',C_OPENSTATUS:'1'}]";

        List sourceObjectMapList = JSONArray.parseArray(sourceObjectJsonStr,HashMap.class);
        List targetObjectMapList = JSONArray.parseArray(targetObjectJsonStr,HashMap.class);

        check(sourceObjectMapList,targetObjectMapList);
        //输出结果
        printCheckResult(sourceObjectMapList);
        printCheckResult(targetObjectMapList);

    }
    public void check(List<Map<String, Object>> sourceObjectMapList,List<Map<String, Object>> targetObjectMapList){
        checkDiffBetweenTwoSystem(sourceObjectMapList,targetObjectMapList, SOURCE_FLAG_A);
        checkDiffBetweenTwoSystem(targetObjectMapList,sourceObjectMapList, SOURCE_FLAG_B);
    }
    private void checkDiffBetweenTwoSystem(List<Map<String, Object>> sourceList, List<Map<String, Object>> targetList, String sourceFlag) {
        //本方没有开放期的情况处理
        if (nullToOneCheck(sourceList, targetList, sourceFlag)){ return;}
        for (Map<String, Object> object: sourceList) {
            try {
                //本方有开放期，对方没有开放期的情况处理
                if (OneToNullCheck(object,targetList, sourceFlag)){ continue;}

                //本方一期和对方一期比对
                for (Map<String, Object> targetObject : targetList) {
                    oneToOneCheck(object,targetObject,sourceFlag);
                }


                //本方一期和对方多期比对
                //如：本方10号到25号开放，对方为1到16号，16号到30号开放的情况。
                //比对思路是将本方拆分成单天比对,且如果有设置工作日，过滤掉非工作日
                if (!object.containsKey("checkresult") || StringUtils.isEmpty(object.get("checkresult").toString())) {
                    if(object.get("C_BEGINDATE").equals(object.get("C_ENDDATE"))){
                        object.put("checkresult", CHECKRESULT_NOTOK);
                        if (SOURCE_FLAG_A.equals(sourceFlag)) {
                            object.put("checkfailreason", CHECKFAILREASON_A);
                        } else {
                            object.put("checkfailreason", CHECKFAILREASON_B);
                        }
                    }else{
                        oneToManyCheck(object,targetList,sourceFlag);
                    }
                }

                //最后，没有匹配上的，设置为比对不一致，独有记录
                if (!object.containsKey("checkresult") || StringUtils.isEmpty(object.get("checkresult").toString())) {
                    object.put("checkresult", CHECKRESULT_NOTOK);
                    if (SOURCE_FLAG_A.equals(sourceFlag)) {
                        object.put("checkfailreason", CHECKFAILREASON_A);
                    } else {
                        object.put("checkfailreason", CHECKFAILREASON_B);
                    }
                }
            } catch (Exception e) {
                //若本次循环比对失败，不影响下一期比对，不抛出异常
                object.put("checkresult", CHECKRESULT_NOTOK);
                object.put("checkfailreason", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private boolean oneToOneCheck(Map<String, Object> objectMap,  Map<String, Object> targetObjectMap,String sourceFlag) {
        if( objectMap.containsKey("isNeedhidden") && true == (Boolean)objectMap.get("isNeedhidden")){
            return true;
        }
        String beginDate = String.valueOf(objectMap.get("C_BEGINDATE"));
        String endDate = String.valueOf(objectMap.get("C_ENDDATE"));
        String openStatus = String.valueOf(objectMap.get("C_OPENSTATUS"));

        String targetBeginDate = String.valueOf(targetObjectMap.get("C_BEGINDATE"));
        String targetEndDate = String.valueOf(targetObjectMap.get("C_ENDDATE"));
        String targetOpenStatus = String.valueOf(targetObjectMap.get("C_OPENSTATUS"));
        //开放状态要一致
        if (!openStatus.equals(targetOpenStatus)) {
            return false;
        }
        //本方与对方记录完全一致
        if (targetBeginDate.equals(beginDate) && targetEndDate.equals(endDate)) {
            //回写本方比对结果
            objectMap.put("checkresult", CHECKRESULT_OK);
            objectMap.put("checkfailreason", "");

            //联带对方也直接记录比对结果
            targetObjectMap.put("checkresult", CHECKRESULT_OK);
            targetObjectMap.put("checkfailreason", "");
            targetObjectMap.put("isNeedhidden",true);
            return true;
        } else if (targetBeginDate.compareTo(beginDate) <= 0 && targetEndDate.compareTo(endDate) >= 0) {
            //对方包含本方记录区间
            //回写本方比对结果,对方仍需继续比对
            objectMap.put("checkresult", CHECKRESULT_OK);
            objectMap.put("checkfailreason", "");
            return true;
        }
        return false;
    }

    private void oneToManyCheck(Map<String, Object> objectMap, List<Map<String, Object>> targetList, String sourceFlag) throws ParseException {

        List<String> list = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String realBeginDate = String.valueOf(objectMap.get("C_BEGINDATE"));
        String realEndDate = String.valueOf(objectMap.get("C_ENDDATE"));
        long s = Long.valueOf(sdf.parse(realBeginDate).getTime());
        long e = Long.valueOf(sdf.parse(realEndDate).getTime());
        //只有结束时间大于开始时间时才进行查询
        if (s <= e) {
            list = findDates(realBeginDate, realEndDate);
        }

        //生成一张按天记录比对结果的清单,Key为每一天，vaue为比对结果
        Map compareResultMap = new HashMap();
        Map<String, Object> copyObject = new HashMap<String, Object>();
        for (String beginDate : list) {

            copyObject.clear();
            copyObject.putAll(objectMap);
            copyObject.put("C_BEGINDATE", beginDate);
            copyObject.put("C_ENDDATE", beginDate);

            //比对并记录结果
            for (Map<String, Object> targetObject : targetList) {
                if (oneToOneCheck(copyObject, targetObject, sourceFlag)) {
                    //记录本次比对结果
                    compareResultMap.put(beginDate, CHECKRESULT_OK);
                    break;
                }
            }
            if (!copyObject.containsKey("checkresult") || StringUtils.isEmpty(copyObject.get("checkresult").toString())) {
                compareResultMap.put(beginDate, CHECKRESULT_NOTOK);
            }
        }

        //分析比对结果
        Boolean checkResult = true;
        for (Object result : compareResultMap.values()) {
            if (result == null || CHECKRESULT_NOTOK.equals(result.toString())) {
                //只要有一条不OK的数据，那么就代表该段为本方独有，结束循环
                checkResult = false;
                break;
            }
        }
        if (!checkResult) {
            objectMap.put("checkresult", CHECKRESULT_NOTOK);
            if (SOURCE_FLAG_A.equals(sourceFlag)) {
                objectMap.put("checkfailreason", CHECKFAILREASON_A);
            } else {
                objectMap.put("checkfailreason", CHECKFAILREASON_B);
            }
        }else{
            objectMap.put("checkresult", CHECKRESULT_OK);
            objectMap.put("isNeedhidden",false);
        }
    }

    public static List<String> findDates(String stime, String etime)
            throws ParseException {
        List<String> allDate = new ArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
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

    private boolean nullToOneCheck(List<Map<String, Object>> sourceList, List<Map<String, Object>> targetList, String sourceFlag) {
        if(sourceList == null|| sourceList.size() <=0){
            if(targetList == null|| targetList.size() <=0){
                return true;
            }
            for (Map<String, Object> targetObject : targetList) {
                targetObject.put("checkresult", CHECKRESULT_NOTOK);
                if (SOURCE_FLAG_A.equals(sourceFlag)) {
                    targetObject.put("checkfailreason", CHECKFAILREASON_B);
                } else {
                    targetObject.put("checkfailreason", CHECKFAILREASON_A);
                }
            }
            return true;
        }
        return false;
    }

    private boolean OneToNullCheck(Map<String, Object> object,List<Map<String, Object>> targetList, String sourceFlag) {
        if(targetList == null || targetList.size() <=0){
            //本方有，对方没有开放期
            object.put("checkresult", CHECKRESULT_NOTOK);
            if (SOURCE_FLAG_A.equals(sourceFlag)) {
                object.put("checkfailreason", CHECKFAILREASON_A);
            } else {
                object.put("checkfailreason", CHECKFAILREASON_B);
            }
            return true;
        }
        return false;
    }

    public String printCheckResult(List<Map<String, Object>> objectMapList) {
        StringBuffer failMsg = new StringBuffer();
        String paramKeyList = "C_BEGINDATE,C_ENDDATE,C_OPENSTATUS,checkresult,checkfailreason";
        List resultList = new ArrayList();

        if(objectMapList != null && objectMapList.size()>0){
            for (Map<String,Object> object:objectMapList ) {
                List paramList = new ArrayList();
                for (String key:paramKeyList.split(",")) {
                    paramList.add(object.get(key));
                    System.out.print(object.get(key)+"\t");
                }
                System.out.println("");
                System.out.println("-----------------------------------------");
                Object[] objects =paramList.toArray();
                resultList.add(objects);
            }
        }


        return failMsg.toString();
    }
}
