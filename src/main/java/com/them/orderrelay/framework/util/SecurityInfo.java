package com.them.orderrelay.framework.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Component
public class SecurityInfo {

    private SecurityInfo() {
    }
    @Value("${spring.security.key-pair}")
    private String keyPair;

    public String encryptAes(final String data) {
        return this.encryptAes(keyPair, data);
    }
    public String encryptAes(final String key, final String data) {
        try {
            byte[] keyData = key.getBytes(StandardCharsets.UTF_8);
            byte[] ivData = key.substring(0, 16).getBytes(StandardCharsets.UTF_8);
            SecretKey secretKey = new SecretKeySpec(keyData, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(ivData));

            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return new String(Base64.getEncoder().encode(encrypted));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException
                | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            log.error("AES Util Encrypt Error",  e);
            return "-1";
        }
    }

    public String decryptAes(final String data) {
        return this.decryptAes(keyPair, data);
    }

    public String decryptAes(final String key, final String encryptedData) {
        byte[] keyData;
        try {
            keyData = key.getBytes(StandardCharsets.UTF_8);
            byte[] ivData = key.substring(0, 16).getBytes(StandardCharsets.UTF_8);
            SecretKey secretKey = new SecretKeySpec(keyData, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(ivData));
            byte[] decrypted = Base64.getDecoder().decode(encryptedData.getBytes(StandardCharsets.UTF_8));
            return new String(cipher.doFinal(decrypted), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException
                | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            log.error("AES Util Decrypt Error", e);
            return "-1";
        }
    }

    /**
     * sha256 암호화
     * @param str
     * @return
     */
    public  String encryptSHA256(String str)
    {
        String SHA = null;
        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-256"); // 이 부분을 SHA-1으로 바꿔도 된다!
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            SHA = sb.toString();
        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return SHA;
    }

    public String encryptBase64(String str)
    {
        return Base64.getEncoder().encodeToString(str.getBytes());
    }
}
