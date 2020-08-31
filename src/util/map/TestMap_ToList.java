package map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author linjing
 * @date: Created in 2020/8/27
 */
public class TestMap_ToList {

    @Test
    public void testMapToList () throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Map> maps = new ArrayList<Map>();
        Map map = new HashMap();
        map.put("C_FUNDCODE","S30B57");
        map.put("基金名称","侯长江1号B");
        map.put("C_MAINFUNDCODE",null);
        map.put("D_CDATE","20150428");
        map.put("销售商","直销");
        map.put("GRQRJE","1,000,000.00");
        map.put("JGQRJE","1,000,000.00");
        map.put("GRQRFE","1,000,220.00");
        map.put("JGQRFE","1,000,220.00");
        map.put("未确认金额","0.00");
        map.put("利息转份额","0.00");
        map.put("利息归基金资产","220.00");
        map.put("ZRS","0.00");
        map.put("C_TRUSTEECODE","615");
        maps.add(map);

        List parse = convertMapsToList(maps,TaatCollectDataSetupEntity.class);
        for (Object t : parse) {
            Method method2 = entity.getClass().getMethod("setCreateDate", String.class);
            method2.invoke(entity, createDate);
        }
    }

    /**
     * 此方法实现JDBCTemplate 返回的Map集合对数据的自动 封装功能 List集合存储着一系列的MAP 对象，obj为一个javaBean
     *
     * @param list
     *            listMap集合
     * @param obj
     *            objjavaBean对象
     * @return
     */
    public List convertMapsToList(List list, Class obj)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        // 生成集合
        ArrayList ary = new ArrayList();
        // 遍历集合中的所有数据
        for (int i = 0; i < list.size(); i++) {
            //// 生成对象实历 将MAP中的所有参数封装到对象中
            Object o = this.convertMapToObject((Map)list.get(i), obj.newInstance());
            // 把对象加入到集合中
            ary.add(o);

        }
        // 返回封装好的集合
        return ary;

    }

    /**
     * Map对象中的值为 name=aaa,value=bbb 调用方法 convertMapToObject(map,user); 将自动将map中的值赋给user类 此方法结合Spring框架的jdbcTemplete将非 常有用
     *
     * @param map
     *            map存储着名称和值集合
     * @param obj
     *            obj要封装的对象
     * @return封装好的对象
     */
    public Object convertMapToObject(Map map, Object obj)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Field[] fields = obj.getClass().getDeclaredFields();
        List array =Arrays.asList(fields);
        Iterator it = array.iterator();
        while (it.hasNext()){
             Field field =(Field)it.next();
            SourceField annotation = field.getAnnotation(SourceField.class);
            if(annotation != null) {
                String mappedName = annotation.name();
                if (!StringUtils.isEmpty(mappedName) && map.containsKey(mappedName)) {
                    // 取得值
                    String value = map.get(mappedName) == null? "":map.get(mappedName).toString();
                    // 取得值的类形
                    Class type = PropertyUtils.getPropertyType(obj, field.getName());
                    if (type != null) {
                        // 设置参数
                        if(type.getName().equals("java.math.BigDecimal")){
                            value= value.replaceAll(",","");
                        }
                        PropertyUtils.setProperty(obj, field.getName(), ConvertUtils.convert(value, type));
                    }
                }
            }
        }
        return obj;
    }



}
