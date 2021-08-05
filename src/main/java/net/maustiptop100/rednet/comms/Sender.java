package net.maustiptop100.rednet.comms;

import net.maustiptop100.rednet.RednetSecurityUtils;
import net.maustiptop100.rednet.RednetUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
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
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, ClassNotFoundException, InvalidAlgorithmParameterException {
        Socket socket = new Socket(this.target, this.port);

        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        PublicKey publicKey = RednetSecurityUtils.bytes2PublicKey((byte[]) in.readObject());

        SecretKey secretKey = RednetSecurityUtils.generateSymmetric();
        out.writeObject(RednetSecurityUtils.wrapSecretKey(secretKey, publicKey));
        IvParameterSpec spec = RednetSecurityUtils.generateParameters();
        out.writeObject(spec.getIV());

        out.writeObject(RednetSecurityUtils.encryptMessage(this.content, secretKey, spec));
    }

}