package net.maustiptop100.rednet.comms;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.function.BiFunction;

public class RednetServer extends Thread {

    private int port;
    private List<BiFunction<byte[], InetAddress, Void>> functionList;

    private boolean running = true;

    public RednetServer(int port, List<BiFunction<byte[], InetAddress, Void>> functionList)
    {
        this.port = port;
        this.functionList = functionList;
    }

    public void run()
    {
        while(this.running)
        {
            try {
                ServerSocket server = new ServerSocket(this.port);
                Socket socket = server.accept();
                new SocketHandler(socket, this.functionList).start();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void shutdown()
    {
        this.running = false;
    }

}
