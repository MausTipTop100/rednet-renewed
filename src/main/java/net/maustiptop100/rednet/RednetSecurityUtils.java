package net.maustiptop100.rednet;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
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

    static IvParameterSpec generateParameters() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    static byte[] publicKey2Bytes(PublicKey publicKey) {
        return publicKey.getEncoded();
    }

    static PublicKey bytes2PublicKey(byte[] data) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyFactory.getInstance(asymmetricKeyAlgorithm).generatePublic(new X509EncodedKeySpec(data));
    }

    static SecretKey unwrapSecretKey(byte[] data, PrivateKey privateKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException
    {
        Cipher cipher = Cipher.getInstance(wrapAlgorithm);
        cipher.init(Cipher.UNWRAP_MODE, privateKey);
        return (SecretKey) cipher.unwrap(data, symmetricKeyAlgorithm, Cipher.SECRET_KEY);
    }

    static byte[] wrapSecretKey(SecretKey secretKey, PublicKey publicKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException
    {
        Cipher cipher = Cipher.getInstance(wrapAlgorithm);
        cipher.init(Cipher.WRAP_MODE, publicKey);
        return cipher.wrap(secretKey);
    }

    static byte[] decryptMessage(byte[] data, SecretKey secretKey, IvParameterSpec spec)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException
    {
        Cipher cipher = Cipher.getInstance(cryptoAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
        return cipher.doFinal(data);
    }

    static byte[] encryptMessage(byte[] data, SecretKey secretKey, IvParameterSpec spec)
            throws IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException
    {
        Cipher cipher = Cipher.getInstance(cryptoAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
        return cipher.doFinal(data);
    }

}
