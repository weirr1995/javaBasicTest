package thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.*;

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
    public  void test() {
        PlateForm plateForm = new PlateForm("美团");
        Restaurant restaurant  = new Restaurant("1","皇帝大");
        restaurant.joinPlateForm(plateForm);
        Customer customer = new Customer("张三");

        TakeOutBoy takeOutBoy = new TakeOutBoy("1","小灰灰");
        takeOutBoy.joinPlateForm(plateForm);

        customer.login(plateForm);
        customer.order("1","稻香鱼");

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
        log.debug("点一份{}",this.order.getWantFoodName());
        Order order = this.plateForm.createOrder(this,this.restaurantId, this.wantFoodName);
        this.order = order;
        log.debug("取餐");
        try{
            fetchFood(order);
        }catch (Exception e){
            log.debug("取餐失败。。。");
        }
        log.debug("拿到外卖{}", food.getFoodName());
        log.debug("开吃！", food.getFoodName());
        eat(food);

    }

    private void fetchFood(Order order) {
        synchronized (order){
            String result= this.plateForm.foodResultToCustomer.get(order);
            notifyAll();
        }
    }

    public void eat(Food food){
        log.debug(food.getFoodName()+"好吃。。。");
    }

}

@Slf4j
class PlateForm {
    private final String plateFormName;
    public LinkedList<Order> orderLinkList = new LinkedList<>();
    public LinkedList<TakeOutBoy> takeOutBoyList = new LinkedList<>();
    public static Map<String,Restaurant> restaurants = new HashMap<>();

    public Object orderLock = new Object();
    public Object takeOutBoyLock = new Object();
    public Object restaurantsLock = new Object();

    public  Map<String, String> foodResultToCustomer = new Hashtable<>();
    public  Map<TakeOutBoy, Food> foodResultToTakOutBoy = new Hashtable<TakeOutBoy, Food>();

    public PlateForm(String plateFormName) {
        this.plateFormName = plateFormName;
    }

    public void acceptRestaurantJoin(Restaurant restaurant){
        this.restaurants.put(restaurant.getRestaurantId(),restaurant);
    }

    /**订单生产者**/
    public  Order createOrder(Customer customer,String restaurantId, String wantFoodName) {
        synchronized (orderLock){
            Order order = new Order(customer,this,restaurantId,wantFoodName);
            this.orderLinkList.push(order);
            notifyAll();
            return order;
        }
    }

    /**订单通知者**/
    public    Order getOrder() {
        synchronized (orderLock){
            while (this.orderLinkList.isEmpty()){
                log.debug("暂时没有订单。。。请等待！");
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Order order = this.orderLinkList.peek();
            notifyAll();
            return order;
        }
    }

    /**订单消费者**/
    public    Order consumOrder() {
        synchronized (orderLock){
            while (this.orderLinkList.isEmpty()){
                log.debug("暂时没有订单。。。请等待！");
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Order order = this.orderLinkList.pop();
            notifyAll();
            return order;
        }
    }

    /**外卖小哥加入**/
    public  TakeOutBoy acceptTakeOutBoy(TakeOutBoy takeOutBoy) {
        synchronized (takeOutBoyLock){
            this.takeOutBoyList.push(takeOutBoy);
            notifyAll();
            return takeOutBoy;
        }
    }


    /** 派送结果 ，客户同步等待**/
    public void notifyCustomer(Order order,String result) {
        synchronized (order){
            this.foodResultToCustomer.put(order.getOrderId(),result);
            notifyAll();
        }
    }


    public void notifyTakeOutBoy(Order order, Food food) {
        TakeOutBoy takeOutBoy = order.getTakeOutBoy();
        sendMsg(takeOutBoy,food);
    }

    private void sendMsg(TakeOutBoy takeOutBoy, Food food) {
        synchronized (takeOutBoy){
            this.foodResultToTakOutBoy.put(takeOutBoy,food);
            notifyAll();
        }
    }
}

@Slf4j
class TakeOutBoy implements Runnable{

    private String id;
    private String name;
    private PlateForm plateForm;
    private Food food;

    public TakeOutBoy(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void joinPlateForm(PlateForm plateForm) {
        this.plateForm = plateForm;
        this.plateForm.acceptTakeOutBoy(this);
    }

    public String getId() {
        return this.id;
    }


    public Food delivery(Order order) {
        String result = "";
        Food food  = fetchFood(order);
        log.debug("努力的送餐中。。。。");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("送外卖{}成功！",food.getFoodName()) ;
        return food;
    }

    private Food fetchFood(Order order) {
        synchronized (this){
            Food food = this.plateForm.foodResultToTakOutBoy.get(this);
            notifyAll();
            return food;
        }
    }

    public void orderCompelete(Order order, Food food) {
        if(food == null){
            this.plateForm.notifyCustomer(order,"送餐失败，送餐过程出现意外！非常抱歉！");
            return;
        }
        this.plateForm.notifyCustomer(order,"送餐成功，您的外卖已到楼下，请自行提取！");
    }

    @Override
    public void run() {
        while(true){
            //抢单
            Order order = this.plateForm.getOrder();
            order.setTakeOutBoy(this);
            //送单
            Food food = delivery(order);
            //抵达联系客户
            orderCompelete(order,food);
        }
    }
}

@Slf4j
class Order{
    private String orderId;
    private Customer customer;
    private PlateForm plateForm;
    private TakeOutBoy takeOutBoy;
    private  Restaurant restaurant;
    private String wantFoodName;


    public Order(Customer customer,PlateForm plateForm,String restaurantId, String customerWantFoodName) {
        this.customer = customer;
        this.plateForm = plateForm;
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
class  Restaurant implements  Runnable {
    private String restaurantId;
    private String restaurantName;
    private PlateForm plateForm;

    public Restaurant(String restaurantId,String restaurantName) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
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
            Order order = this.plateForm.consumOrder();
            log.debug("拿到了{}订单",order.getOrderId());
            try{
                Food food = cook(order);
                notifyTakeOutBoy(order,food);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void notifyTakeOutBoy(Order order, Food food) {
        this.plateForm.notifyTakeOutBoy(order,food);
    }

    public void joinPlateForm(PlateForm plateForm) {
        plateForm.acceptRestaurantJoin(this);
        this.plateForm = plateForm;
    }


    private Food cook(Order order) throws InterruptedException {
        Thread.sleep(new Random().nextInt(2000));
        Food food = new Food(order.getWantFoodName());
        log.debug(order.getWantFoodName()+"炒好了！");
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
