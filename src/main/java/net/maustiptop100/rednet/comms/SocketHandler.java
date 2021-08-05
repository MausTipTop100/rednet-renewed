package net.maustiptop100.rednet.comms;

import net.maustiptop100.rednet.RednetSecurityUtils;
import net.maustiptop100.rednet.RednetUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyPair;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class SocketHandler extends Thread {

    private Socket socket;
    private List<BiConsumer<byte[], InetAddress>> execute;

    SocketHandler(Socket socket, List<BiConsumer<byte[], InetAddress>> exec)
    {
        this.socket = socket;
        this.execute = exec;
    }

    public void run()
    {
        try {
            ObjectOutputStream out = new ObjectOutputStream(this.socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(this.socket.getInputStream());

            // KEY COMMUNICATION

            KeyPair pair = RednetSecurityUtils.generateAsymmetric();
            out.writeObject(RednetSecurityUtils.publicKey2Bytes(pair.getPublic()));

            SecretKey secretKey = RednetSecurityUtils.unwrapSecretKey((byte[]) in.readObject(), pair.getPrivate());
            IvParameterSpec spec = new IvParameterSpec((byte[]) in.readObject());

            // MESSAGE COMMUNICATION

            byte[] message = RednetSecurityUtils.decryptMessage((byte[]) in.readObject(), secretKey, spec);

            // EVENT DISPATCHING

            this.execute.forEach(function -> function.accept(message, socket.getInetAddress()));

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Oops, something went wrong while handling socket :(");
        }
    }

}
