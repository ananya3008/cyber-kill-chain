import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;

public class RansomwareDecryption {
    // Decrypt file using AES and provided key
    public static void decryptFile(File encryptedFile, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedBytes = Files.readAllBytes(encryptedFile.toPath());
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        // Write decrypted data to a new file
        FileOutputStream outputStream = new FileOutputStream(encryptedFile.getName().replace(".encrypted", ".decrypted"));
        outputStream.write(decryptedBytes);
        outputStream.close();
    }

    // Convert hex string back to byte array
    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return bytes;
    }

    public static void main(String[] args) {
        try {
            // Encrypted file to be decrypted
            File encryptedFile = new File("important_document.txt.encrypted");
            // Encryption key provided by the attacker (simulated as input)
            String keyHex = "YOUR_HEX_KEY_HERE"; // Replace with the actual key you sent to the attacker
            byte[] decodedKey = hexToBytes(keyHex);
            SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
            // Decrypt the file
            decryptFile(encryptedFile, secretKey);
            System.out.println("File decrypted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}