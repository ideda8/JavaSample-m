package socket;

import java.io.*;
import java.net.Socket;

/**
 * 套接字客户端示例
 * 以下GreetingClient.java代码是一个客户端程序，它通过使用套接字连接到服务器并发送问候语，然后等待响应。
 */
public class GreetingClient {

    public static void main(String [] args) {
        String serverName = "127.0.0.1";
        int port = Integer.parseInt("8888");
        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF("Hello from " + client.getLocalSocketAddress());
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);

            System.out.println("Server says " + in.readUTF());
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
