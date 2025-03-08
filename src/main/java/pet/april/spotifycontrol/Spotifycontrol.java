package pet.april.spotifycontrol;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Spotifycontrol implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("spotifycontrol");

    @Override
    public void onInitialize() {
        try {
            LOGGER.info("SpotifyControl mod initialized");
        } catch (Exception e) {
            LOGGER.error("Failed to initialize SpotifyControl: " + e.getMessage());
        }
    }
}