package IO.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author linjing
 * @date: Created in 2020/8/14
 */

public class TCPClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 124);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("我是客户端".getBytes());
        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[100];
        inputStream.read(bytes);// 阻塞
        System.out.println("服务端说：" + new String(bytes));
        socket.close();
    }

}