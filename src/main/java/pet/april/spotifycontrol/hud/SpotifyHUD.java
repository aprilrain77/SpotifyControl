package pet.april.spotifycontrol.hud;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Psapi;
import com.sun.jna.ptr.IntByReference;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpotifyHUD implements HudRenderCallback {
    private static final String SPOTIFY_CLASS_NAME = "Chrome_WidgetWin_1";
    private static final Pattern SONG_PATTERN = Pattern.compile("(.*?) - (.*?)( - Spotify)?$");

    private static final String MUSIC_ICON = "♬";
    private static final String PAUSED_ICON = "⏸";

    public enum PositionPreset {
        TOP_LEFT,
        TOP_CENTER,
        TOP_RIGHT,
        MIDDLE_LEFT,
        MIDDLE_CENTER,
        MIDDLE_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        BOTTOM_RIGHT
    }

    private String songTitle = "Not playing";
    private String artist = "";
    private boolean isPlaying = false;
    private boolean visible = true;
    private boolean showWhenNotPlaying = true;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final boolean isWindows;
    private final boolean isLinux;

    private int xPos = 10;
    private int yPos = 10;
    private int bgColor = 0x80000000;
    private int textColor = 0xFFFFFFFF;
    private int margin = 10;
    private PositionPreset currentPreset = PositionPreset.TOP_LEFT;
    private boolean presetPositionApplied = false;

    private int scrollPosition = 0;
    private long lastScrollTime = 0;
    private int scrollSpeed = 2;
    private int scrollDelay = 50;
    private int maxTextWidth = 200;
    private boolean enableScrolling = true;

    public SpotifyHUD() {
        String osName = System.getProperty("os.name").toLowerCase();
        isWindows = osName.contains("win");
        isLinux = osName.contains("linux") || osName.contains("unix");

        if (isLinux) {
            checkPlayerctlAvailable();
        }

        scheduler.scheduleAtFixedRate(this::updateCurrentSongAsync, 0, 1, TimeUnit.SECONDS);
        this.currentPreset = PositionPreset.TOP_LEFT;
    }

    @Override
    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || !visible || isDebugHudOpen()) {
            return;
        }

        if (!isPlaying && !showWhenNotPlaying) {
            return;
        }

        String displayText;
        if (isPlaying) {
            displayText = MUSIC_ICON + " " + artist + " - " + songTitle;
        } else {
            displayText = PAUSED_ICON + " " + songTitle;
        }

        int textWidth = client.textRenderer.getWidth(displayText);
        int padding = 4;

        boolean needsScrolling = enableScrolling && textWidth > maxTextWidth;

        int actualDisplayWidth = needsScrolling ? maxTextWidth : textWidth;

        int totalWidth = actualDisplayWidth + padding * 2;
        int totalHeight = client.textRenderer.fontHeight + padding * 2;

        if (!presetPositionApplied) {
            applyPositionPreset(totalWidth, totalHeight);
            presetPositionApplied = true;
        }

        context.fill(xPos - padding, yPos - padding,
                xPos + actualDisplayWidth + padding, yPos + client.textRenderer.fontHeight + padding,
                bgColor);

        if (needsScrolling) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastScrollTime > scrollDelay) {
                scrollPosition += scrollSpeed;
                lastScrollTime = currentTime;

                if (scrollPosition > textWidth) {
                    scrollPosition = -maxTextWidth;
                }
            }

            int scissorX = xPos;
            int scissorY = yPos;
            int scissorWidth = actualDisplayWidth;
            int scissorHeight = client.textRenderer.fontHeight;

            context.enableScissor(scissorX, scissorY, scissorX + scissorWidth, scissorY + scissorHeight);

            context.drawText(client.textRenderer, displayText, xPos - scrollPosition, yPos, textColor, true);

            context.disableScissor();
        } else {
            context.drawText(client.textRenderer, displayText, xPos, yPos, textColor, true);

            scrollPosition = 0;
        }
    }

    private boolean isDebugHudOpen() {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            return client != null && client.getDebugHud() != null && client.getDebugHud().shouldShowDebugHud();
        } catch (Exception e) {
            return false;
        }
    }

    public void setPositionPreset(PositionPreset preset) {
        this.currentPreset = preset;
        this.presetPositionApplied = false;
    }

    private void applyPositionPreset(int hudWidth, int hudHeight) {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.getWindow() == null) return;

            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();

            switch (currentPreset) {
                case TOP_LEFT:
                    this.xPos = margin;
                    this.yPos = margin;
                    break;
                case TOP_CENTER:
                    this.xPos = (screenWidth - hudWidth) / 2;
                    this.yPos = margin;
                    break;
                case TOP_RIGHT:
                    this.xPos = screenWidth - hudWidth - margin;
                    this.yPos = margin;
                    break;
                case MIDDLE_LEFT:
                    this.xPos = margin;
                    this.yPos = (screenHeight - hudHeight) / 2;
                    break;
                case MIDDLE_CENTER:
                    this.xPos = (screenWidth - hudWidth) / 2;
                    this.yPos = (screenHeight - hudHeight) / 2;
                    break;
                case MIDDLE_RIGHT:
                    this.xPos = screenWidth - hudWidth - margin;
                    this.yPos = (screenHeight - hudHeight) / 2;
                    break;
                case BOTTOM_LEFT:
                    this.xPos = margin;
                    this.yPos = screenHeight - hudHeight - margin;
                    break;
                case BOTTOM_CENTER:
                    this.xPos = (screenWidth - hudWidth) / 2;
                    this.yPos = screenHeight - hudHeight - margin;
                    break;
                case BOTTOM_RIGHT:
                    this.xPos = screenWidth - hudWidth - margin;
                    this.yPos = screenHeight - hudHeight - margin;
                    break;
            }
        } catch (Exception e) {
            this.xPos = margin;
            this.yPos = margin;
        }
    }

    public void updateCurrentSong() {
        updateCurrentSongAsync();
    }

    private void updateCurrentSongAsync() {
        try {
            if (isWindows) {
                updateFromWindows();
            } else if (isLinux) {
                updateFromLinux();
            } else {
                songTitle = "Not playing";
                artist = "";
                isPlaying = false;
            }
        } catch (Exception e) {
            songTitle = "Error: " + e.getMessage();
            artist = "";
            isPlaying = false;
        }

        if (currentPreset != PositionPreset.TOP_LEFT) {
            presetPositionApplied = false;
        }
    }

    private void updateFromWindows() {
        try {
            String windowTitle = findSpotifyWindowTitle();
            if (windowTitle != null && !windowTitle.isEmpty()) {
                parseSpotifyTitle(windowTitle);
                isPlaying = true;
            } else {
                songTitle = "Not playing";
                artist = "";
                isPlaying = false;
            }
        } catch (Exception e) {
            songTitle = "Spotify error";
            artist = "";
            isPlaying = false;
        }
    }

    private void updateFromLinux() {
        try {
            Process isPlayingProcess = Runtime.getRuntime().exec(new String[] {
                    "playerctl", "--player=spotify", "status"
            });

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(isPlayingProcess.getInputStream()))) {
                String status = reader.readLine();
                isPlaying = status != null && status.equalsIgnoreCase("Playing");

                if (!isPlaying) {
                    songTitle = "Not playing";
                    artist = "";
                    return;
                }
            }

            Process artistProcess = Runtime.getRuntime().exec(new String[] {
                    "playerctl", "--player=spotify", "metadata", "artist"
            });

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(artistProcess.getInputStream()))) {
                artist = reader.readLine();
                if (artist == null) artist = "";
            }

            Process titleProcess = Runtime.getRuntime().exec(new String[] {
                    "playerctl", "--player=spotify", "metadata", "title"
            });

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(titleProcess.getInputStream()))) {
                songTitle = reader.readLine();
                if (songTitle == null) songTitle = "Unknown";
            }
        } catch (IOException e) {
            songTitle = "Error: " + e.getMessage();
            artist = "";
            isPlaying = false;
        }
    }

    private void checkPlayerctlAvailable() {
        try {
            Process process = Runtime.getRuntime().exec(new String[] {"which", "playerctl"});
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                System.err.println("playerctl not found. Please install playerctl to use Spotify integration on Linux.");
                System.err.println("Install with: sudo apt-get install playerctl (Ubuntu/Debian) or sudo dnf install playerctl (Fedora)");
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error checking for playerctl: " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void parseSpotifyTitle(String title) {
        if (title == null) return;

        Matcher matcher = SONG_PATTERN.matcher(title);
        if (matcher.matches()) {
            artist = matcher.group(1).trim();
            songTitle = matcher.group(2).trim();
        } else {
            songTitle = title;
            artist = "";
        }
    }

    private String findSpotifyWindowTitle() {
        final String[] result = new String[1];

        try {
            User32.INSTANCE.EnumWindows((hWnd, data) -> {
                try {
                    char[] className = new char[256];
                    User32.INSTANCE.GetClassName(hWnd, className, 256);
                    String classNameStr = Native.toString(className);

                    if (SPOTIFY_CLASS_NAME.equals(classNameStr)) {
                        IntByReference processIdRef = new IntByReference();
                        User32.INSTANCE.GetWindowThreadProcessId(hWnd, processIdRef);
                        int processId = processIdRef.getValue();

                        if (isSpotifyProcess(processId)) {
                            char[] windowText = new char[512];
                            User32.INSTANCE.GetWindowText(hWnd, windowText, 512);
                            String title = Native.toString(windowText);

                            if (title != null && !title.isEmpty() && title.contains(" - ")) {
                                if (User32.INSTANCE.IsWindowVisible(hWnd)) {
                                    result[0] = title;
                                    return false;
                                }
                            }
                        }
                    }
                    return true;
                } catch (Exception e) {
                    return true;
                }
            }, null);
        } catch (Exception e) {
            result[0] = null;
        }

        return result[0];
    }

    private boolean isSpotifyProcess(int processId) {
        try {
            Kernel32 kernel32 = Kernel32.INSTANCE;
            WinNT.HANDLE processHandle = kernel32.OpenProcess(
                    Kernel32.PROCESS_QUERY_INFORMATION |
                            Kernel32.PROCESS_VM_READ,
                    false, processId);

            if (processHandle == null) {
                return false;
            }

            try {
                char[] pathBuffer = new char[1024];
                Psapi.INSTANCE.GetModuleFileNameExW(
                        processHandle, null, pathBuffer, pathBuffer.length);
                String path = Native.toString(pathBuffer);

                return path != null && path.toLowerCase().contains("spotify");
            } finally {
                kernel32.CloseHandle(processHandle);
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void shutdown() {
        scheduler.shutdownNow();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                System.err.println("Spotify HUD thread pool did not terminate");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void setPosition(int x, int y) {
        this.xPos = x;
        this.yPos = y;
    }

    public void setColors(int background, int text) {
        this.bgColor = background;
        this.textColor = text;
    }

    public boolean toggleVisibility() {
        this.visible = !this.visible;
        return this.visible;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setMargin(int margin) {
        this.margin = margin;
        this.presetPositionApplied = false;
    }

    public int getBackgroundColor() {
        return this.bgColor;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setMaxTextWidth(int width) {
        this.maxTextWidth = Math.max(50, width);
        this.presetPositionApplied = false;
    }

    public void setScrollSpeed(int pixelsPerScroll) {
        this.scrollSpeed = Math.max(1, pixelsPerScroll);
    }

    public void setScrollDelay(int milliseconds) {
        this.scrollDelay = Math.max(10, milliseconds);
    }

    public void setEnableScrolling(boolean enable) {
        this.enableScrolling = enable;
        this.presetPositionApplied = false;
    }

    public int getMaxTextWidth() {
        return this.maxTextWidth;
    }

    public boolean isScrollingEnabled() {
        return this.enableScrolling;
    }

    public void setShowWhenNotPlaying(boolean show) {
        this.showWhenNotPlaying = show;
    }

    public boolean isShowingWhenNotPlaying() {
        return this.showWhenNotPlaying;
    }
}