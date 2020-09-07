package rmi;

/**
 * @author linjing
 * @date: Created in 2020/9/4
 */

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * 远程接口实现,继承UnicastRemoteObject
 * @author lxz
 *
 */
public class RemoteImpl extends UnicastRemoteObject  implements IRemote{
    public RemoteImpl()throws RemoteException{}//构造方法
    public String show()throws RemoteException{//调用方法实现
        System.out.println("进入");
        System.out.println(this.toString());
        return "远程调用成功";
    }
}