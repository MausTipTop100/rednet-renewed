package net.maustiptop100.rednet;

import net.maustiptop100.rednet.comms.RednetServer;
import net.maustiptop100.rednet.comms.Sender;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.InetAddress;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Vector;
import java.util.function.BiFunction;

public class Rednet {

    private int port;

    private List<BiFunction<byte[], InetAddress, Void>> functionList = new Vector<>();
    private RednetServer server;

    private Rednet() {
        this.server = new RednetServer(this.port, this.functionList);
        this.server.start();
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
    public void onReceive(BiFunction<byte[], InetAddress, Void> function) {
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
