package thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @version V1.0
 * @author: zouwh
 * @description: 点外卖
 * @date: 2020/7/9 11:01 PM
 * @Copyright: 2020 www.ztzqzg.com Ltd. All rights reserved.
 * 注意：本内容仅限于中泰证券（上海）资产管理有限公司内部传阅，禁止外泄以及用于其他的商业项目
 */
@Slf4j
public class TestDelivery {

    @Test
    public  void test() throws InterruptedException {
        String orderName ="粥";
        PlateForm plateForm = new PlateForm();
        Customer customer = new Customer("张三");
        customer.login(plateForm);
        customer.want(orderName);
        customer.order();
        plateForm.createOrder(customer.getOrderId());
    }

}


/*
* 居民
*/
@Slf4j
class Customer extends Thread{
    private String customerName;
    private String orderName ;
    private int orderId ;
    private  PlateForm plateForm;

    public Customer(String customerName) {
        this.customerName = customerName;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public  void login(PlateForm plateForm){
        this.plateForm = plateForm;
    }
    public int want(String orderName){
        this.orderName = orderName;
        return orderId;
    }

    @Override
    public void run(){
        log.debug("点一份{}",this.orderName);
        Food food = waitFood();
        log.debug("拿到外卖{}", food.getFoodName());

    }


    public Food waitFood(){
        Food food = (Food)this.plateForm.notifyCustomer();
        return food;

    }

    public void order() {
        this.orderId = this.plateForm.generateId();
        this.start();
    }
}


class PlateForm {
    private static Map<Integer, Result> boxex = new Hashtable<>();

    private static int id = 1;

    private Map<Integer,Result> resultMap = new HashMap();

    public synchronized static int generateId(){
         return id++;
    }

    public Food createOrder(int orderId) throws InterruptedException {
        TakeOutBoy takeOutBoy =assignTakeOutBoy(orderId);
        takeOutBoy.delivery(orderId);
        Result  result = resultMap.get(orderId);
        return (Food)result.get();
    }


    public TakeOutBoy assignTakeOutBoy(int orderId){
        return new TakeOutBoy(orderId);
    }


    public void notifyCustomer(Result result) {
        this.resultMap.put(result.getId(),result);
    }
}

/*
* 外卖小哥
*/
@Slf4j
class TakeOutBoy{

    private PlateForm plateForm;
    private int orderId;


    TakeOutBoy(int  orderId){
        this.plateForm = plateForm;
        this.orderId = orderId;
    }


    public void delivery(int orderId) throws InterruptedException {
        String orderName = getOrderNameById(orderId);
        Food  food = new Food(orderName);
        Result result =  new Result(orderId);
        result.setGoods(food);
        synchronized (this){
            try {
                Thread.sleep(1000);
                result.setResult("放到您的楼下快递柜了，请自行提取！");
                notifyCustomer(result);
                log.debug("送外卖{}成功！",((Food)result.getGoods()).getFoodName()) ;
                this.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
                result.setResult("其他突发事件，无法送达！快递员稍后会联系您，请耐心等待！");
                notifyCustomer(result);
            }
        }
    }

    private String getOrderNameById(int orderId) {
        return "粥";
    }

    private void notifyCustomer(Result result) {
        this.plateForm.notifyCustomer(result);
    }

}

class    Food{
    private String foodName ;

    public Food(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodName() {
        return foodName;
    }

}


@Slf4j
 class Result {
    private int id;
    private Object goods;
    private Object result;

    public Result(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Object getGoods() {
        return goods;
    }

    public void setGoods(Object goods) {
        this.goods = goods;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object get(){
        synchronized (this){
            while (true){
                if(result == null){
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return this.result;
            }
        }

    }

}