package rmi;

/**
 * @author linjing
 * @date: Created in 2020/9/4
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * 远程接口,实现Remote
 * @author lxz
 *
 */
public interface IRemote extends Remote{

    public String show()throws RemoteException;//声明方法
}