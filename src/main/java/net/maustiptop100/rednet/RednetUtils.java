package net.maustiptop100.rednet;

import java.io.*;

public interface RednetUtils {

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
