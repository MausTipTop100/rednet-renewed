package net.maustiptop100.rednet;

import javax.crypto.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public interface RednetSecurityUtils {

    String wrapAlgorithm = "RSA/ECB/OAEPWithSHA1AndMGF1Padding";
    String cryptoAlgorithm = "AES/CBC/PKCS5Padding";
    String symmetricKeyAlgorithm = "AES";
    String asymmetricKeyAlgorithm = "RSA";

    static KeyPair generateAsymmetric() throws NoSuchAlgorithmException {
        return KeyPairGenerator.getInstance(asymmetricKeyAlgorithm).generateKeyPair();
    }

    static SecretKey generateSymmetric() throws NoSuchAlgorithmException {
        return KeyGenerator.getInstance(symmetricKeyAlgorithm).generateKey();
    }

    static int publicKey2Int(PublicKey publicKey) {
        return RednetUtils.getIntFromBytes(publicKey.getEncoded());
    }

    static PublicKey int2PublicKey(int data) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyFactory.getInstance(asymmetricKeyAlgorithm).generatePublic(new X509EncodedKeySpec(RednetUtils.getBytesFromInt(data)));
    }

    static SecretKey unwrapSecretKeyFromInt(int data, PrivateKey privateKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException
    {
        byte[] bytes = RednetUtils.getBytesFromInt(data);
        Cipher cipher = Cipher.getInstance(wrapAlgorithm);
        cipher.init(Cipher.UNWRAP_MODE, privateKey);
        return (SecretKey) cipher.unwrap(bytes, symmetricKeyAlgorithm, Cipher.SECRET_KEY);
    }

    static int wrapSecretKeyToInt(SecretKey secretKey, PublicKey publicKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException
    {
        Cipher cipher = Cipher.getInstance(wrapAlgorithm);
        cipher.init(Cipher.WRAP_MODE, publicKey);
        return RednetUtils.getIntFromBytes(cipher.wrap(secretKey));
    }

    static byte[] decryptMessage(int data, SecretKey secretKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher cipher = Cipher.getInstance(cryptoAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(RednetUtils.getBytesFromInt(data));
    }

    static int encryptMessage(byte[] data, SecretKey secretKey)
            throws IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException
    {
        Cipher cipher = Cipher.getInstance(cryptoAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return RednetUtils.getIntFromBytes(cipher.doFinal(data));
    }

}
