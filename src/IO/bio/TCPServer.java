package IO.bio;

import lombok.val;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author linjing
 * @date: Created in 2020/8/14
 */

public class TCPServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(124);
        ExecutorService executor = Executors.newFixedThreadPool(100);
        while (!Thread.currentThread().isInterrupted()) {
            Socket socket = serverSocket.accept();
            executor.submit(new ConnectIOnHandler(socket));
        }
    }

}

 class ConnectIOnHandler implements Runnable {
    private Socket socket;
    public ConnectIOnHandler(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted() && !socket.isClosed()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                OutputStream outputStream = socket.getOutputStream();
                PrintWriter pr = new PrintWriter(outputStream, true);
                String request;
                String response;

                while ((request = br.readLine()) != null) {
                    if ("bye".equals(request)) {
                        socket.close();
                        break;
                    }
                    // 处理request，构造response
                    System.out.println("客户端("+socket.getInetAddress()+":"+socket.getPort()+Thread.currentThread().getName()+") Message: " + request);
                    response = request;
                    pr.println(response);
                    outputStream.write(("server response :"+response).getBytes());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
