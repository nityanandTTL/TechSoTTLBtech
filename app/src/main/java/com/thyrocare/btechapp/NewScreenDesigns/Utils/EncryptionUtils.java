package com.thyrocare.btechapp.NewScreenDesigns.Utils;

import android.util.Base64;

import com.thyrocare.btechapp.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtils {



    private SecretKey secretKey;

    public EncryptionUtils() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);

            secretKey = keyGenerator.generateKey();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    // ------------------------------------------------TODO Encryption type 1-------------------------------------------------

    public byte[] makeAes(byte[] rawMessage, int cipherMode) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(cipherMode, this.secretKey);
            byte[] output = cipher.doFinal(rawMessage);
            return output;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // ------------------------------------------------TODO Encryption type 2 (AES - symmetric)-------------------------------------------------

    public static SecretKey secret;
    public static String password;

    public static SecretKey generateKey()
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return secret = new SecretKeySpec(password.getBytes(), "AES");
    }

    public static byte[] encryptMsg(String message, SecretKey secret)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
    {
        /* Encrypt the message. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return cipherText;
    }

    public static String decryptMsg(byte[] cipherText, SecretKey secret)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException
    {
        /* Decrypt the message, given derived encContentValues and initialization vector. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
        return decryptString;
    }


    // ------------------------------------------------TODO Encryption type 2 (RSA - Asymmetric)-------------------------------------------------

    public  static Key publicKey = null;
    public  static Key privateKey = null;

    public  static void RSAKeyPair(){

        // Generate key pair for 2048-bit RSA encryption and decryption
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.genKeyPair();
            publicKey = kp.getPublic();
            privateKey = kp.getPrivate();
        } catch (Exception e) {
            MessageLogger.LogError("Crypto", "RSA key pair error");
        }

    }

    public static void GenerateEncodedString( String targetString){
        // Encode the original data with the RSA private key
        byte[] encodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE, privateKey);
            encodedBytes = c.doFinal(targetString.getBytes());
        } catch (Exception e) {
            MessageLogger.LogError("Crypto", "RSA encryption error");
        }
        if (BuildConfig.DEBUG) {
            MessageLogger.LogDebug("Encoded string: ", new String(Base64.encodeToString(encodedBytes, Base64.DEFAULT)));
        }
    }

    public static void GenerateDecodedString( byte[] encodedBytes){
        // Encode the original data with the RSA private key
        // Decode the encoded data with the RSA public key
        byte[] decodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE, publicKey);
            decodedBytes = c.doFinal(encodedBytes);
        } catch (Exception e) {
            MessageLogger.LogError("Crypto", "RSA decryption error");
        }

        if (BuildConfig.DEBUG){
            MessageLogger.LogDebug("Decoded string: ", new String(decodedBytes));
        }

    }

// ------------------------------------------------TODO Encryption type 3 (base 64)-------------------------------------------------

    public static String EncodeString64(String stringToEncode){
        String base64 = null;
        try {
            byte[] data = stringToEncode.getBytes("UTF-8");
            base64 = Base64.encodeToString(data, Base64.DEFAULT);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return  base64;
    }

    public static String DecodeString64(String stringToDecode){
        String decodedString = "";
        try {
            byte[] data = Base64.decode(stringToDecode, Base64.DEFAULT);
            decodedString = new String(data, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return  decodedString;
    }

    // ------------------------------------------------TODO Encryption type 4 (base 64 + AES Cipher)-------------------------------------------------

    private static final String ALGO = "AES";
    private static final byte[] keyValue = new byte[]{'T', 'h', 'y', 'r', 'o', 'c', 'a', 'r', 'e', '$', '1', '2', '3', '4', '5', '6'};

    public static String encryptSSL(String data)  {
        String StrEncrypt = "";
        try {
            Key key = generateSSLKey();
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(data.getBytes());
            StrEncrypt = Base64.encodeToString(encVal,Base64.DEFAULT);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return StrEncrypt;
    }

    public static String decryptSSL(String encryptedData)  {
        String decryptedValue = "";
        try {
            Key key = generateSSLKey();
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue =  Base64.decode(encryptedData, Base64.DEFAULT);
            byte[] decValue = c.doFinal(decordedValue);
            decryptedValue = new String(decValue);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return decryptedValue;
    }

    private static Key generateSSLKey()  {
        Key key = null;
        try {
            key = new SecretKeySpec(keyValue, ALGO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return key;
    }

}
