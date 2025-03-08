package pet.april.spotifycontrol.controller;

import java.io.IOException;

public class LinuxSpotifyController {
    public void playPause() {
        try {
            executePlayerctlCommand("play-pause");
        } catch (Exception e) {
            System.err.println("Error in Linux playPause: " + e.getMessage());
        }
    }

    public void nextTrack() {
        try {
            executePlayerctlCommand("next");
        } catch (Exception e) {
            System.err.println("Error in Linux nextTrack: " + e.getMessage());
        }
    }

    public void previousTrack() {
        try {
            executePlayerctlCommand("previous");
        } catch (Exception e) {
            System.err.println("Error in Linux previousTrack: " + e.getMessage());
        }
    }

    private void executePlayerctlCommand(String command) {
        executePlayerctlCommand(command, null);
    }

    private void executePlayerctlCommand(String command, String arg) {
        try {
            String[] cmd;
            if (arg != null) {
                cmd = new String[] {"playerctl", "--player=spotify", command, arg};
            } else {
                cmd = new String[] {"playerctl", "--player=spotify", command};
            }

            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute playerctl command", e);
        }
    }
}