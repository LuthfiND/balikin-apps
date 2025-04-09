package org.balikin.util;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@ApplicationScoped
@Slf4j
public class AESUtil {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_SIZE = 12;
    private static final int TAG_LENGTH = 128;

    private static final String SECRET_KEY = "BALIKIN123456789"; // 16-byte key (Fix)

    public String encrypt(String data) throws Exception {
        // Generate IV secara acak
        byte[] iv = new byte[IV_SIZE];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);

        // Inisialisasi Cipher dengan AES-GCM
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new GCMParameterSpec(TAG_LENGTH, iv));

        // Enkripsi data
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // Gabungkan IV dengan hasil enkripsi
        byte[] combined = new byte[iv.length + encryptedData.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedData, 0, combined, iv.length, encryptedData.length);

        // Kembalikan hasil dalam format Base64
        return Base64.getEncoder().encodeToString(combined);
    }

    public String decrypt(String data) throws Exception {
        // Decode Base64 ke byte array
        byte[] decodedData = Base64.getDecoder().decode(data);

        // Pisahkan IV dan data terenkripsi
        byte[] iv = new byte[IV_SIZE];
        byte[] encryptedData = new byte[decodedData.length - IV_SIZE];

        System.arraycopy(decodedData, 0, iv, 0, IV_SIZE);
        System.arraycopy(decodedData, IV_SIZE, encryptedData, 0, encryptedData.length);

        // Inisialisasi kembali Cipher dengan AES-GCM
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new GCMParameterSpec(TAG_LENGTH, iv));

        // Dekripsi data
        byte[] decryptedData = cipher.doFinal(encryptedData);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }
}
