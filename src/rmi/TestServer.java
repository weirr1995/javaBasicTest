package rmi;

/**
 * @author linjing
 * @date: Created in 2020/9/4
 */

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *  服务端启动,创建端口上的对象注册列表,向对象注册表中注册远程调用对象
 * @author lxz
 *
 */
public class TestServer {
    public static void main(String[] args) throws MalformedURLException, RemoteException, AlreadyBoundException, InterruptedException {
        RemoteImpl r = new RemoteImpl();//创建远程对象
        Registry rr = LocateRegistry.createRegistry(1234); //创建1234端口上的对象注册表,如果已经创建了就用getRegistry方法获取
        rr.bind("testrmi", r);//向注册表中注册对象
        System.out.println(r.toString());
    }
}