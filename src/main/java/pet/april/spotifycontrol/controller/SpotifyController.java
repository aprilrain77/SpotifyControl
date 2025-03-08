package pet.april.spotifycontrol.controller;

public class SpotifyController {
    private final boolean isWindows;
    private final boolean isLinux;
    private final WindowsSpotifyController windowsController;
    private final LinuxSpotifyController linuxController;

    public SpotifyController() {
        String osName = System.getProperty("os.name").toLowerCase();
        isWindows = osName.contains("win");
        isLinux = osName.contains("linux") || osName.contains("unix");

        windowsController = isWindows ? new WindowsSpotifyController() : null;
        linuxController = isLinux ? new LinuxSpotifyController() : null;
    }

    public void playPause() {
        try {
            if (isWindows && windowsController != null) {
                windowsController.playPause();
            } else if (isLinux && linuxController != null) {
                linuxController.playPause();
            }
        } catch (Exception e) {
            System.err.println("Error in playPause: " + e.getMessage());
        }
    }

    public void nextTrack() {
        try {
            if (isWindows && windowsController != null) {
                windowsController.nextTrack();
            } else if (isLinux && linuxController != null) {
                linuxController.nextTrack();
            }
        } catch (Exception e) {
            System.err.println("Error in nextTrack: " + e.getMessage());
        }
    }

    public void previousTrack() {
        try {
            if (isWindows && windowsController != null) {
                windowsController.previousTrack();
            } else if (isLinux && linuxController != null) {
                linuxController.previousTrack();
            }
        } catch (Exception e) {
            System.err.println("Error in previousTrack: " + e.getMessage());
        }
    }
}