package net.maustiptop100.rednet;

import net.maustiptop100.rednet.comms.RednetServer;
import net.maustiptop100.rednet.comms.Sender;

import java.net.InetAddress;
import java.util.List;
import java.util.Vector;
import java.util.function.BiConsumer;

public class Rednet {

    private int port;

    private List<BiConsumer<byte[], InetAddress>> functionList = new Vector<>();
    private RednetServer server;

    private Rednet() {

    }

    public void send(byte[] data, String receiver) {
        try {
            new Sender(data, receiver, this.port).apply();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Something went wrong while sending the message :(");
        }
    }

    /**
     * Adds a listener function
     */
    public void onReceive(BiConsumer<byte[], InetAddress> function) {
        this.functionList.add(function);
    }

    /**
     * Initialises a rednet instance with the default port '4456'
     * @return Returns a rednet instance
     */
    public static Rednet getInstance()
    {
        Rednet rednet = new Rednet();
        rednet.port = 4456;

        rednet.server = new RednetServer(rednet.port, rednet.functionList);
        rednet.server.start();

        return rednet;
    }

    /**
     * Initialises a rednet instance with a custom port
     * @return Returns a rednet instance
     */
    public static Rednet getInstance(int port)
    {
        Rednet rednet = new Rednet();
        rednet.port = port;

        return rednet;
    }

}
