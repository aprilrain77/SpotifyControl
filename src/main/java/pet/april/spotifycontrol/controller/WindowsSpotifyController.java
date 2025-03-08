package pet.april.spotifycontrol.controller;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.WString;

public class WindowsSpotifyController {
    private static final int APPCOMMAND_MEDIA_PLAY_PAUSE = 0xE0000;
    private static final int APPCOMMAND_MEDIA_NEXTTRACK = 0xB0000;
    private static final int APPCOMMAND_MEDIA_PREVIOUSTRACK = 0xC0000;
    private static final int WM_APPCOMMAND = 0x319;

    public interface User32 extends Library {
        User32 INSTANCE = Native.load("user32", User32.class);
        int SendMessageW(int hWnd, int msg, int wParam, int lParam);
        int FindWindowW(WString lpClassName, WString lpWindowName);
    }

    public void playPause() {
        try {
            sendMediaCommand(APPCOMMAND_MEDIA_PLAY_PAUSE);
        } catch (Exception e) {
            System.err.println("Error in Windows playPause: " + e.getMessage());
        }
    }

    public void nextTrack() {
        try {
            sendMediaCommand(APPCOMMAND_MEDIA_NEXTTRACK);
        } catch (Exception e) {
            System.err.println("Error in Windows nextTrack: " + e.getMessage());
        }
    }

    public void previousTrack() {
        try {
            sendMediaCommand(APPCOMMAND_MEDIA_PREVIOUSTRACK);
        } catch (Exception e) {
            System.err.println("Error in Windows previousTrack: " + e.getMessage());
        }
    }

    private void sendMediaCommand(int command) {
        try {
            int hwnd = User32.INSTANCE.FindWindowW(null, null);
            User32.INSTANCE.SendMessageW(hwnd, WM_APPCOMMAND, 0, command);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send media command", e);
        }
    }
}