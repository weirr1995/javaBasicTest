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
        int orderNo = orderBoxes.generateId();



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
        log.debug("点了一份{}",this.orderName);
        int orderId  = this.plateForm.receiveOrder();
        Food food = get(orderId);
        log.debug("拿到外卖{}", food.getFoodName());

    }

    public Food get(int id){
        return (Food) this.plateForm.get(id);
    }

}


class OrderBoxes{
    private static Map<Integer,GuardedObject2> boxex = new Hashtable<>();

    private static int id = 1;

    public synchronized static int generateId(){
        return id++;
    }

    public int  receiveOrder(){
        GuardedObject2  guardedObject2  = new GuardedObject2(OrderBoxes.generateId());
        boxex.put(guardedObject2.getId(),guardedObject2);
        return guardedObject2.getId();
    }

    public Food get(int id){
        GuardedObject2  guardedObject2  = boxex.get(id);
        Food food = (Food)guardedObject2.get();
        return food;
    }
}

/*
* 外卖xiaoge
*/
class TakeOutBoy{


}

class    Food{
    private String foodName ;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }


}


@Slf4j
 class GuardedObject2{
    private int id;
    private Object result;

    public GuardedObject2(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void delivery(Object object){
        synchronized (this){
            try {
                Thread.sleep(1000);
                this.result =object;
                log.debug("送外卖成功！") ;
                this.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
                this.result = "其他突发事件，未及时送达！";
            }
        }
    }
}