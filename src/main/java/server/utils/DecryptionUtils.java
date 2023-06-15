package server.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DecryptionUtils {
    private static final String AES_ALGORITHM = "AES";

    public static String decrypt(String cipherText, String key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        SecretKey secretKey = new SecretKeySpec(keyBytes, AES_ALGORITHM);

        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] encryptedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
