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
    public  void test() throws InterruptedException {
        PlateForm plateForm = new PlateForm("美团");
        Restaurant restaurant  = new Restaurant("1","皇帝大");
        restaurant.joinPlateForm(plateForm);
        Customer customer = new Customer("张三");

        TakeOutBoy takeOutBoy = new TakeOutBoy("1","小灰灰");
        takeOutBoy.joinPlateForm(plateForm);

        customer.login(plateForm);
        customer.order("1","稻香鱼");
        Food food = customer.fetchFood();
        customer.eat(food);

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
            this.food= this.plateForm.fetchFood(order);
        }catch (Exception e){
            log.debug("取餐失败。。。");
        }
        log.debug("拿到外卖{}", food.getFoodName());
        log.debug("开吃！", food.getFoodName());
        eat(food);

    }

    public void eat(Food food){
        log.debug(food.getFoodName()+"好吃。。。");
    }

    public Food fetchFood() {
        return this.plateForm.fetchFood(this.order);
    }
}

@Slf4j
class PlateForm implements Runnable {
    private final String plateFormName;
    public LinkedList<Order> orderLinkList = new LinkedList<>();
    public static Map<String,Restaurant> restaurants = new HashMap<>();
    public static Map<String,TakeOutBoy> takeoutBoyMap = new HashMap<>();
    public static Map<String, Food> resultMap = new Hashtable<>();

    public PlateForm(String plateFormName) {
        this.plateFormName = plateFormName;
    }

    public void acceptRestaurantJoin(Restaurant restaurant){
        this.restaurants.put(restaurant.getRestaurantId(),restaurant);
    }

    public void acceptTakeOutBoyJoin(TakeOutBoy takeOutBoy) {
        this.takeoutBoyMap.put(takeOutBoy.getId(),takeOutBoy);
    }

    public   void acceptOrder(Customer customer,String restaurantId, String wantFoodName) {
        Order order = createOrder(customer, restaurantId, wantFoodName);
        this.orderLinkList.push(order);
        notifyAll();
    }

    public Order createOrder(Customer customer,String restaurantId, String wantFoodName) {
        Order order = new Order(customer,this,restaurantId,wantFoodName);
        this.orderLinkList.push(order);
        return order;
    }

    public  synchronized  Order assignOrder() {
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

    public void orderCompelete(Order order, Food food) {
        this.resultMap.put(order.getOrderId(),food);
    }

    public Food fetchFood(Order order) {
        return (Food)this.resultMap.get(order.getOrderId());
    }

    @Override
    public void run() {
        while (true){
            if(!this.orderLinkList.isEmpty()){
                this.orderLinkList.poll()
            }
            //分配订单
            //完成订单，通知客户
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
        this.plateForm.acceptTakeOutBoyJoin(this);
    }

    public String getId() {
        return this.id;
    }


    public void delivery(Order order) {
        String result = "";
        this.food  = order.getRestaurant().fetchFood(order);
        log.debug("努力的送餐中。。。。");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("送外卖{}成功！",this.food.getFoodName()) ;
        this.notifyAll();
    }

    private void notifyCustomer(String result,Order order) {
        this.plateForm.orderCompelete(order,result,this.food);
    }


    public void compelete(Order order) {
        String result = "放到您的楼下快递柜了，请自行提取！";
        notifyCustomer(result,order);
    }

    @Override
    public void run() {
        while(true){
            //抢单
            Order order = this.plateForm.assignOrder();
            //送单
            delivery(order);
            //抵达联系客户
            compelete(order);

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
            Order order = this.plateForm.assignOrder();
            log.debug("拿到了{}订单",order.getOrderId());
            try{
                cook(order);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

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
