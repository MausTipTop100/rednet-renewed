package net.maustiptop100.rednet.comms;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class RednetServer extends Thread {

    private int port;
    private List<BiConsumer<byte[], InetAddress>> functionList;

    private boolean running = true;

    public RednetServer(int port, List<BiConsumer<byte[], InetAddress>> functionList)
    {
        this.port = port;
        this.functionList = functionList;
    }

    public void run()
    {
        try {
            ServerSocket server = new ServerSocket(this.port);
            while(this.running)
            {
                Socket socket = server.accept();
                new SocketHandler(socket, this.functionList).start();
            }
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown()
    {
        this.running = false;
    }

}
