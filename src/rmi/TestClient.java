package rmi;

/**
 * @author linjing
 * @date: Created in 2020/9/4
 */

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * 客户端启动,获得远程的对象注册表中的对象引用
 * @author lxz
 *
 */
public class TestClient {

    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
        IRemote r = (IRemote) Naming.lookup("rmi://localhost:1234/testrmi");//获取远程1234端口对象注册表中testrmi的stub
        String a = r.show();//调用引用的方法,实际上调用的是stub,由stub与服务端交互并返回结果
        System.out.println(a);
    }
}