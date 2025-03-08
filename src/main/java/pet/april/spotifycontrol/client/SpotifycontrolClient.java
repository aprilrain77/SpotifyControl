package pet.april.spotifycontrol.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import pet.april.spotifycontrol.controller.SpotifyController;
import pet.april.spotifycontrol.hud.SpotifyHUD;
import pet.april.spotifycontrol.command.SpotifyCommands;

public class SpotifycontrolClient implements ClientModInitializer {
    private static final SpotifyHUD SPOTIFY_HUD = new SpotifyHUD();
    private static final SpotifyController SPOTIFY_CONTROLLER = new SpotifyController();
    private static SpotifyCommands SPOTIFY_COMMANDS;

    private static KeyBinding playPauseKey;
    private static KeyBinding nextTrackKey;
    private static KeyBinding prevTrackKey;
    private static KeyBinding toggleHudKey;

    @Override
    public void onInitializeClient() {
        try {
            HudRenderCallback.EVENT.register(SPOTIFY_HUD);

            registerKeybindings();

            SPOTIFY_COMMANDS = new SpotifyCommands(SPOTIFY_HUD);

            net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(client -> {
                try {
                    if (playPauseKey.wasPressed()) {
                        SPOTIFY_CONTROLLER.playPause();
                    }

                    if (nextTrackKey.wasPressed()) {
                        SPOTIFY_CONTROLLER.nextTrack();
                    }

                    if (prevTrackKey.wasPressed()) {
                        SPOTIFY_CONTROLLER.previousTrack();
                    }

                    if (toggleHudKey.wasPressed()) {
                        SPOTIFY_HUD.toggleVisibility();
                    }
                } catch (Exception e) {
                    System.err.println("Error handling keybindings: " + e.getMessage());
                }
            });

            ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
                try {
                    SPOTIFY_HUD.shutdown();
                } catch (Exception e) {
                    System.err.println("Error shutting down Spotify HUD: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            System.err.println("Error initializing Spotify Control: " + e.getMessage());
        }
    }

    private void registerKeybindings() {
        try {
            playPauseKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.spotifycontrol.playpause",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_J,
                    "category.spotifycontrol.spotify"
            ));

            nextTrackKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.spotifycontrol.nexttrack",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_K,
                    "category.spotifycontrol.spotify"
            ));

            prevTrackKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.spotifycontrol.prevtrack",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_H,
                    "category.spotifycontrol.spotify"
            ));

            toggleHudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.spotifycontrol.togglehud",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_U,
                    "category.spotifycontrol.spotify"
            ));
        } catch (Exception e) {
            System.err.println("Error registering keybindings: " + e.getMessage());
        }
    }
}