package net.maustiptop100.rednet.comms;

import net.maustiptop100.rednet.RednetSecurityUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public class Sender {

    private byte[] content;
    private String target;
    private int port;

    public Sender(byte[] content, String target, int port)
    {
        this.content = content;
        this.target = target;
        this.port = port;
    }

    public void apply()
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException {
        Socket socket = new Socket(this.target, this.port);

        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();

        PublicKey publicKey = RednetSecurityUtils.int2PublicKey(in.read());

        SecretKey secretKey = RednetSecurityUtils.generateSymmetric();
        out.write(RednetSecurityUtils.wrapSecretKeyToInt(secretKey, publicKey));

        out.write(RednetSecurityUtils.encryptMessage(this.content, secretKey));
    }

}