package net.maustiptop100.rednet;

import java.io.*;
import java.nio.ByteBuffer;

public interface RednetUtils {

    static byte[] getBytesFromInt(int data) {
        return ByteBuffer.allocate(4).putInt(data).array();
    }

    static int getIntFromBytes(byte[] data) {
        return ByteBuffer.wrap(data).getInt();
    }

    static Serializable bytes2Serializable(byte[] bytes)
            throws IOException, ClassNotFoundException, ClassCastException
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);

        Serializable object = (Serializable) ois.readObject();
        ois.close();
        return object;
    }

    static byte[] serializable2Bytes(Serializable object)
            throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.close();

        return baos.toByteArray();
    }

}
