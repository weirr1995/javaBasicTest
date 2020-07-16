package concurrent.thread;

import lombok.extern.slf4j.Slf4j;

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
public class Delivery {

    public static void main(String[] args) {
        OrderBoxes orderBoxes = new OrderBoxes();
        new Person().order("粥",orderBoxes).start();
        TakeOutBoy takeOutBoy =orderBoxes.distributTakeOutBoy();
        takeOutBoy.delivery();

    }

}



/*
* 居民
*/
@Slf4j
class Person extends Thread{
    private String orderName ;
    private OrderBoxes plateForm;

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public OrderBoxes getPlateForm() {
        return plateForm;
    }

    public void setPlateForm(OrderBoxes plateForm) {
        this.plateForm = plateForm;
    }


    public Person order(String orderName, OrderBoxes plateForm){
        this.orderName = orderName;
        this.plateForm = plateForm;
        return this;
    }

    @Override
    public void run(){
        log.debug("点一份{}",this.orderName);
        int orderId = order(this.orderName);
        Food food = waitFood(orderId);
        log.debug("拿到外卖{}", food.getFoodName());

    }

    public int order(String orderName){
        int orderId  = this.plateForm.createOrder(orderName);
        return orderId;
    }

    public Food waitFood(int id){
        GuardedObject2 guardedObject2= this.plateForm.getGuardedObject2ById(id);
        Food food = (Food)guardedObject2.get();
        return food;

    }

}


class OrderBoxes{
    private static Map<Integer,GuardedObject2> boxex = new Hashtable<>();

    private static int id = 1;

    public synchronized static int generateId(){
         return id++;
    }

    public int createOrder(String orderName){
        GuardedObject2  guardedObject2  = new GuardedObject2(OrderBoxes.generateId());
        guardedObject2.setGoods(orderName);
        boxex.put(guardedObject2.getId(),guardedObject2);
        return guardedObject2.getId();
    }

    public GuardedObject2 getGuardedObject2ById(int id){
        GuardedObject2  guardedObject2  = boxex.get(id);
        return guardedObject2;
    }

    public TakeOutBoy distributTakeOutBoy() {
        GuardedObject2  guardedObject2  = getGuardedObject2ById(id);
        return new TakeOutBoy(id,this,((Food)guardedObject2.getGoods()).getFoodName());
    }
}

/*
* 外卖小哥
*/
@Slf4j
class TakeOutBoy{

    private OrderBoxes plateForm;
    private int orderId;
    private String foodName;


    TakeOutBoy(int  orderId,OrderBoxes plateForm,String foodName){
        this.plateForm = plateForm;
        this.orderId = orderId;
        this.foodName = foodName;
    }

    public void delivery(){
        Food  food = new Food(this.foodName);
        GuardedObject2 guardedObject2 =  this.plateForm.getGuardedObject2ById(this.orderId);
        synchronized (this){
            try {
                guardedObject2.setResult(food);
                Thread.sleep(1000);
                log.debug("送外卖{}成功！",((Food)guardedObject2.getGoods()).getFoodName()) ;
                this.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
                guardedObject2.setResult("其他突发事件，未及时送达！");;
            }
        }
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
 class GuardedObject2{
    private int id;
    private Object goods;
    private Object result;

    public GuardedObject2(int id) {
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