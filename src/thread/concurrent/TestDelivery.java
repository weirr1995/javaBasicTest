package thread.concurrent;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

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
    public  void test() throws IOException {
        PlateForm plateForm = new PlateForm("美团");


        Restaurant restaurant1  = new Restaurant("1","皇帝大");
        restaurant1.joinPlateForm(plateForm);
        restaurant1.start();

        Restaurant restaurant2  = new Restaurant("2","wagas");
        restaurant2.joinPlateForm(plateForm);
        restaurant2.start();


        TakeOutBoy takeOutBoy1 = new TakeOutBoy("1","小灰灰");
        takeOutBoy1.joinPlateForm(plateForm);
        takeOutBoy1.start();

        TakeOutBoy takeOutBoy2 = new TakeOutBoy("2","林静");
        takeOutBoy2.joinPlateForm(plateForm);
        takeOutBoy2.start();


        for(int i=0;i<1;i++){
            Customer customer1 = new Customer("张三");
            customer1.login(plateForm);
           customer1.order("1","稻香鱼"+i);

           /*  Customer customer2 = new Customer("李四");
            customer2.login(plateForm);
            customer2.order("1","蛋炒饭"+i);

            Customer customer3 = new Customer("何YY");
            customer3.login(plateForm);
            customer3.order("2","牛排"+i);
            Customer customer4 = new Customer("王XX");
            customer4.login(plateForm);
            customer4.order("2","饮料"+i);*/
        }


        System.in.read();

    }
}

@Slf4j
class Customer extends  Thread{
    private String customerName;
    private Order order ;
    private  PlateForm plateForm;
    private String restaurantId;
    private String wantFoodName;
    private  Food food;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public PlateForm getPlateForm() {
        return plateForm;
    }

    public void setPlateForm(PlateForm plateForm) {
        this.plateForm = plateForm;
    }

    public Customer(String customerName) {
        this.customerName = customerName;
        this.setName(customerName);
    }

    public  void login(PlateForm plateForm){
        this.plateForm = plateForm;
    }

    public  void order(String restaurantId,String wantFoodName){
        this.restaurantId = restaurantId;
        this.wantFoodName = wantFoodName;
        this.start();
    }

    @Override
    public void run(){
        Order order = this.plateForm.createOrder(this,this.restaurantId, this.wantFoodName);
        log.info("点一份{}，订单编号{}",this.wantFoodName,order.getOrderId());
        this.order = order;
        try{
            food =fetchFood(order);
        }catch (Exception e){
            log.debug("取餐失败。。。");
        }
        log.info("拿到外卖{},订单编号{}", food.getFoodName(),order.getOrderId());
        log.debug("开吃！{}，订单编号:{}", food.getFoodName(),order.getOrderId());
        eat(food);

    }

    private Food fetchFood(Order order) throws InterruptedException {
        synchronized (this.order.foodLock){
            while (!this.plateForm.customerFoodBox.containsKey(order)){
                this.order.foodLock.wait();
            }
            Food food = this.plateForm.customerFoodBox.get(order);
            if(food == null){
                String exceptionMsg = this.plateForm.errorOrderCenter.get(order);
                log.debug(exceptionMsg);
            }
            return food;
        }

    }

    public void eat(Food food){
        log.debug(food.getFoodName()+"好吃。。。");
    }

}

@Slf4j
class PlateForm {
    private final String plateFormName;

    public Object plateWorkLock =  new Object();
    Long lastUsedOrderId = 0L;

    public Set<TakeOutBoy> takeOutBoys = new HashSet<>();
    public  Map<String,Restaurant> restaurants = new HashMap<>();

    public Hashtable<Restaurant, LinkedBlockingQueue> orderQueueForRestrauants = new Hashtable<>();
    public BlockingQueue<Order> orderQueueForTackOutBoy = new LinkedBlockingDeque<>();
    public Object ordeLockForTakeOutBoy = new Object();

    public Map<Order, Food> customerFoodBox = new Hashtable<Order, Food>();

    public Map<Order, String> errorOrderCenter = new Hashtable<Order, String>();
    public Object errorOrderLock = new Object();


    public PlateForm(String plateFormName) {
        this.plateFormName = plateFormName;
    }

    public void acceptRestaurantJoin(Restaurant restaurant){
        this.restaurants.put(restaurant.getRestaurantId(),restaurant);
    }

    /**订单生产者**/
    public  Order createOrder(Customer customer,String restaurantId, String wantFoodName) {
        Restaurant restaurant = this.restaurants.get(restaurantId);
        Order order = new Order(customer,this,restaurantId,wantFoodName);
        synchronized (restaurant.orderLock){
            try {
                if(!this.orderQueueForRestrauants.containsKey(restaurant)){
                    LinkedBlockingQueue<Order> orderQueueForEachRestrauant = new LinkedBlockingQueue<>();
                    this.orderQueueForRestrauants.put(restaurant,orderQueueForEachRestrauant);
                }
                this.orderQueueForRestrauants.get(restaurant).put(order);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            restaurant.orderLock.notifyAll();
        }

        synchronized (this.ordeLockForTakeOutBoy){
            this.orderQueueForTackOutBoy.offer(order);
            this.ordeLockForTakeOutBoy.notifyAll();
        }

        return order;
    }

    /**订单通知者**/
    public    Order getOrder() {
        synchronized (ordeLockForTakeOutBoy){
            while (this.orderQueueForTackOutBoy.isEmpty()){
                try {
                    ordeLockForTakeOutBoy.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Order order = this.orderQueueForTackOutBoy.poll();
            return order;
        }
    }

    /**订单消费者**/
    public    Order consumOrder(Restaurant restaurant) {
        synchronized (restaurant.orderLock){
            while (!this.orderQueueForRestrauants.containsKey(restaurant) ||  this.orderQueueForRestrauants.get(restaurant).isEmpty() ){
                try {
                    restaurant.orderLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            LinkedBlockingQueue<Order> linkedBlockingQueue = this.orderQueueForRestrauants.get(restaurant);
            Order order = linkedBlockingQueue.poll();
            return order;
        }
    }

    /**外卖小哥加入**/
    public  TakeOutBoy acceptTakeOutBoy(TakeOutBoy takeOutBoy) {
        this.takeOutBoys.add(takeOutBoy);
        return takeOutBoy;
    }


    /** 派送结果 ，客户同步等待**/
    public void notifyAllCustomerFetchFood(Order order, Food food) {
        synchronized (order.foodLock){
            this.customerFoodBox.put(order,food);
            order.foodLock.notifyAll();
        }
    }

    public void getOrderExceptionCause(Order order, String exceptionMsg) throws InterruptedException {
        synchronized (order){
            while (true){
                if(!this.errorOrderCenter.containsKey(order)){
                    order.wait();
                }
                String exceptionCause = this.errorOrderCenter.get(order);
                log.debug(exceptionCause);
            }
        }
    }

    public void notifyAllPlateFormDiliveryFailed(Order order, String cause) {
        synchronized (order){
            this.errorOrderCenter.put(order,cause);
            notifyAll();
        }
    }

    public String generateOrderId() {
        synchronized (plateWorkLock){
            return "order"+(++lastUsedOrderId);
        }
    }
}

@Slf4j
class TakeOutBoy extends  Thread{

    private String id;
    private String name;
    private PlateForm plateForm;
    private Food food;

    public TakeOutBoy(String id, String name) {
        this.id = id;
        this.name = name;
        this.setName(name);

    }

    public void joinPlateForm(PlateForm plateForm) {
        this.plateForm = plateForm;
        this.plateForm.acceptTakeOutBoy(this);
    }

    public Food delivery(Order order) {
        String result = "";
        Food food  = null;
        log.debug("外卖员取餐中"+order.getOrderId()+"。。。。");
        try {
            food = fetchFood(order);
        } catch (InterruptedException e) {
            e.printStackTrace();
            this.plateForm.notifyAllPlateFormDiliveryFailed(order,e.getMessage());
        }
        log.debug("努力的送餐中"+order.getOrderId()+"。。。。");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.plateForm.notifyAllCustomerFetchFood(order,food);
        log.debug("送外卖{}成功！订单编号：{}",food.getFoodName(),order.getOrderId()) ;
        return food;
    }

    private Food fetchFood(Order order) throws InterruptedException {
        synchronized (order.getRestaurant().foodAreaLock){
            while (!order.getRestaurant().foodArea.containsKey(order)){
                order.getRestaurant().foodAreaLock.wait();
            }
            Food food = order.getRestaurant().foodArea.remove(order);
            return food;
        }
    }

    private Order getOrder(){
        Order order = this.plateForm.getOrder();
        return order;
    }
    @Override
    public void run() {
        while(true){
            //抢单
            Order order = getOrder();
            if(order == null) continue;
            log.debug(this.getName()+"抢到了订单！{}",order.getOrderId());
            order.setTakeOutBoy(this);
            //送单
            delivery(order);
        }
    }
}

@Slf4j
class Order{
    public Object foodLock = new Object();
    public Object ordeLockForRestrauant = new Object();
    private String orderId;
    private Customer customer;
    private PlateForm plateForm;
    private TakeOutBoy takeOutBoy;
    private  Restaurant restaurant;
    private String wantFoodName;


    public Order(Customer customer,PlateForm plateForm,String restaurantId, String customerWantFoodName) {
        this.customer = customer;
        this.plateForm = plateForm;
        String orderId = this.plateForm.generateOrderId();
        this.orderId = orderId;
        this.restaurant = this.plateForm.restaurants.get(restaurantId);
        this.wantFoodName = customerWantFoodName;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public PlateForm getPlateForm() {
        return plateForm;
    }

    public void setPlateForm(PlateForm plateForm) {
        this.plateForm = plateForm;
    }

    public TakeOutBoy getTakeOutBoy() {
        return takeOutBoy;
    }

    public void setTakeOutBoy(TakeOutBoy takeOutBoy) {
        this.takeOutBoy = takeOutBoy;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getWantFoodName() {
        return this.wantFoodName ;
    }
}

@Slf4j
class  Restaurant extends   Thread {
    private String restaurantId;
    private String restaurantName;
    private PlateForm plateForm;

    public  Map<Order, Food> foodArea = new Hashtable<Order, Food>();
    public Object foodAreaLock = new Object();

    public Object orderLock = new Object();

    public Restaurant(String restaurantId,String restaurantName) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.setName(restaurantName);
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    @Override
    public void run() {
        while (true){
            //拿订单
            Order order =  getNewOrder(this.plateForm);
            log.debug("拿到了订单{}，订单编号{}",order.getWantFoodName(),order.getOrderId());
            //炒菜
             Food food = cook(order);
           //通知取餐
           notifyAllTakeOutBoy(order,food);
        }
    }


    public void notifyAllTakeOutBoy(Order order, Food food) {
        synchronized (this.foodAreaLock){
            this.foodArea.put(order,food);
            log.debug(order.getWantFoodName()+"炒好了！订单编号{}",order.getOrderId());
            this.foodAreaLock.notifyAll();
        }

    }


    private Order getNewOrder(PlateForm plateForm) {
        Order order = this.plateForm.consumOrder(this);
        return order;
    }


    public void joinPlateForm(PlateForm plateForm) {
        plateForm.acceptRestaurantJoin(this);
        this.plateForm = plateForm;
    }


    private Food cook(Order order) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Food food = new Food(order.getWantFoodName());
        return food;
    }

}

@Slf4j
class    Food{
    private String foodName ;

    public Food(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodName() {
        return foodName;
    }

}
