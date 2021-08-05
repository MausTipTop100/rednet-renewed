package net.maustiptop100.rednet.comms;

import net.maustiptop100.rednet.RednetSecurityUtils;

import javax.crypto.SecretKey;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyPair;
import java.util.List;
import java.util.function.BiFunction;

public class SocketHandler extends Thread {

    private Socket socket;
    private List<BiFunction<byte[], InetAddress, Void>> execute;

    SocketHandler(Socket socket, List<BiFunction<byte[], InetAddress, Void>> exec)
    {
        this.execute = exec;
    }

    public void run()
    {
        try {
            OutputStream out = this.socket.getOutputStream();
            InputStream in = this.socket.getInputStream();

            // KEY COMMUNICATION

            KeyPair pair = RednetSecurityUtils.generateAsymmetric();
            out.write(RednetSecurityUtils.publicKey2Int(pair.getPublic()));

            SecretKey secretKey = RednetSecurityUtils.unwrapSecretKeyFromInt(in.read(), pair.getPrivate());

            // MESSAGE COMMUNICATION

            byte[] message = RednetSecurityUtils.decryptMessage(in.read(), secretKey);

            // EVENT DISPATCHING

            this.execute.forEach(function -> function.apply(message, socket.getInetAddress()));

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Oops, something went wrong while handling socket :(");
        }
    }

}
