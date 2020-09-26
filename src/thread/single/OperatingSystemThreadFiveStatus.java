package thread.single;

/**
 * @version V1.0
 * @author: zouwh
 * @description: 操作系统层面线程的五种线程状态 新建 可运行  运行  阻塞 终止
 * @date: 2020/7/5 2:35 PM
 * @Copyright: 2020 www.ztzqzg.com Ltd. All rights reserved.
 * 注意：本内容仅限于中泰证券（上海）资产管理有限公司内部传阅，禁止外泄以及用于其他的商业项目
 */
public class OperatingSystemThreadFiveStatus {
    public static void main(String[] args) {
        //新建
        Thread thread1 = new Thread();
        //运行
        Thread thread2 = new Thread(()->{
                while (true){
                }
        });
        thread2.start();
        Thread thread3 = new Thread(()->{

        });
        thread3.start();
        Thread thread4 = new Thread(()->{

        });
        Thread thread5 = new Thread(()->{

        });
    }
}
