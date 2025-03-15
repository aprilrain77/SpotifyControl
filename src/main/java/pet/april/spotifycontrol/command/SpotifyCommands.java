package pet.april.spotifycontrol.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import pet.april.spotifycontrol.hud.SpotifyHUD;
import pet.april.spotifycontrol.hud.SpotifyHUD.PositionPreset;

public class SpotifyCommands {
    private final SpotifyHUD spotifyHUD;

    public SpotifyCommands(SpotifyHUD spotifyHUD) {
        this.spotifyHUD = spotifyHUD;
        registerCommands();
    }

    private void registerCommands() {
        try {
            ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
                register(dispatcher);
            });
        } catch (Exception e) {
            System.err.println("Error registering commands: " + e.getMessage());
        }
    }

    private void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        try {
            dispatcher.register(
                    ClientCommandManager.literal("spotify")
                            .then(ClientCommandManager.literal("hud")
                                    .then(ClientCommandManager.literal("position")
                                            .then(ClientCommandManager.argument("x", IntegerArgumentType.integer(0, 2000))
                                                    .then(ClientCommandManager.argument("y", IntegerArgumentType.integer(0, 2000))
                                                            .executes(context -> {
                                                                try {
                                                                    int x = IntegerArgumentType.getInteger(context, "x");
                                                                    int y = IntegerArgumentType.getInteger(context, "y");
                                                                    spotifyHUD.setPosition(x, y);
                                                                    context.getSource().sendFeedback(Text.literal("§aSpotify HUD position set to X: " + x + ", Y: " + y));
                                                                } catch (Exception e) {
                                                                    context.getSource().sendFeedback(Text.literal("§cError setting position: " + e.getMessage()));
                                                                }
                                                                return 1;
                                                            })
                                                    )
                                            )
                                    )
                                    .then(ClientCommandManager.literal("preset")
                                            .then(ClientCommandManager.literal("topleft")
                                                    .executes(context -> {
                                                        try {
                                                            spotifyHUD.setPositionPreset(PositionPreset.TOP_LEFT);
                                                            context.getSource().sendFeedback(Text.literal("§aSpotify HUD position set to top left"));
                                                        } catch (Exception e) {
                                                            context.getSource().sendFeedback(Text.literal("§cError setting position: " + e.getMessage()));
                                                        }
                                                        return 1;
                                                    })
                                            )
                                            .then(ClientCommandManager.literal("topcenter")
                                                    .executes(context -> {
                                                        try {
                                                            spotifyHUD.setPositionPreset(PositionPreset.TOP_CENTER);
                                                            context.getSource().sendFeedback(Text.literal("§aSpotify HUD position set to top center"));
                                                        } catch (Exception e) {
                                                            context.getSource().sendFeedback(Text.literal("§cError setting position: " + e.getMessage()));
                                                        }
                                                        return 1;
                                                    })
                                            )
                                            .then(ClientCommandManager.literal("topright")
                                                    .executes(context -> {
                                                        try {
                                                            spotifyHUD.setPositionPreset(PositionPreset.TOP_RIGHT);
                                                            context.getSource().sendFeedback(Text.literal("§aSpotify HUD position set to top right"));
                                                        } catch (Exception e) {
                                                            context.getSource().sendFeedback(Text.literal("§cError setting position: " + e.getMessage()));
                                                        }
                                                        return 1;
                                                    })
                                            )
                                            .then(ClientCommandManager.literal("middleleft")
                                                    .executes(context -> {
                                                        try {
                                                            spotifyHUD.setPositionPreset(PositionPreset.MIDDLE_LEFT);
                                                            context.getSource().sendFeedback(Text.literal("§aSpotify HUD position set to middle left"));
                                                        } catch (Exception e) {
                                                            context.getSource().sendFeedback(Text.literal("§cError setting position: " + e.getMessage()));
                                                        }
                                                        return 1;
                                                    })
                                            )
                                            .then(ClientCommandManager.literal("middlecenter")
                                                    .executes(context -> {
                                                        try {
                                                            spotifyHUD.setPositionPreset(PositionPreset.MIDDLE_CENTER);
                                                            context.getSource().sendFeedback(Text.literal("§aSpotify HUD position set to middle center"));
                                                        } catch (Exception e) {
                                                            context.getSource().sendFeedback(Text.literal("§cError setting position: " + e.getMessage()));
                                                        }
                                                        return 1;
                                                    })
                                            )
                                            .then(ClientCommandManager.literal("middleright")
                                                    .executes(context -> {
                                                        try {
                                                            spotifyHUD.setPositionPreset(PositionPreset.MIDDLE_RIGHT);
                                                            context.getSource().sendFeedback(Text.literal("§aSpotify HUD position set to middle right"));
                                                        } catch (Exception e) {
                                                            context.getSource().sendFeedback(Text.literal("§cError setting position: " + e.getMessage()));
                                                        }
                                                        return 1;
                                                    })
                                            )
                                            .then(ClientCommandManager.literal("bottomleft")
                                                    .executes(context -> {
                                                        try {
                                                            spotifyHUD.setPositionPreset(PositionPreset.BOTTOM_LEFT);
                                                            context.getSource().sendFeedback(Text.literal("§aSpotify HUD position set to bottom left"));
                                                        } catch (Exception e) {
                                                            context.getSource().sendFeedback(Text.literal("§cError setting position: " + e.getMessage()));
                                                        }
                                                        return 1;
                                                    })
                                            )
                                            .then(ClientCommandManager.literal("bottomcenter")
                                                    .executes(context -> {
                                                        try {
                                                            spotifyHUD.setPositionPreset(PositionPreset.BOTTOM_CENTER);
                                                            context.getSource().sendFeedback(Text.literal("§aSpotify HUD position set to bottom center"));
                                                        } catch (Exception e) {
                                                            context.getSource().sendFeedback(Text.literal("§cError setting position: " + e.getMessage()));
                                                        }
                                                        return 1;
                                                    })
                                            )
                                            .then(ClientCommandManager.literal("bottomright")
                                                    .executes(context -> {
                                                        try {
                                                            spotifyHUD.setPositionPreset(PositionPreset.BOTTOM_RIGHT);
                                                            context.getSource().sendFeedback(Text.literal("§aSpotify HUD position set to bottom right"));
                                                        } catch (Exception e) {
                                                            context.getSource().sendFeedback(Text.literal("§cError setting position: " + e.getMessage()));
                                                        }
                                                        return 1;
                                                    })
                                            )
                                    )
                                    .then(ClientCommandManager.literal("toggle")
                                            .executes(context -> {
                                                try {
                                                    boolean visible = spotifyHUD.toggleVisibility();
                                                    String status = visible ? "visible" : "hidden";
                                                    context.getSource().sendFeedback(Text.literal("§aSpotify HUD is now " + status));
                                                } catch (Exception e) {
                                                    context.getSource().sendFeedback(Text.literal("§cError toggling visibility: " + e.getMessage()));
                                                }
                                                return 1;
                                            })
                                    )
                                    .then(ClientCommandManager.literal("margin")
                                            .then(ClientCommandManager.argument("pixels", IntegerArgumentType.integer(0, 100))
                                                    .executes(context -> {
                                                        try {
                                                            int margin = IntegerArgumentType.getInteger(context, "pixels");
                                                            spotifyHUD.setMargin(margin);
                                                            context.getSource().sendFeedback(Text.literal("§aSpotify HUD margin set to " + margin + " pixels"));
                                                        } catch (Exception e) {
                                                            context.getSource().sendFeedback(Text.literal("§cError setting margin: " + e.getMessage()));
                                                        }
                                                        return 1;
                                                    })
                                            )
                                    )
                                    .then(ClientCommandManager.literal("color")
                                            .then(ClientCommandManager.literal("background")
                                                    .then(ClientCommandManager.argument("hexcolor", StringArgumentType.word())
                                                            .executes(context -> {
                                                                try {
                                                                    String hexColor = StringArgumentType.getString(context, "hexcolor");
                                                                    int color = parseHexColor(hexColor);
                                                                    spotifyHUD.setColors(color, spotifyHUD.getTextColor());
                                                                    context.getSource().sendFeedback(Text.literal("§aSpotify HUD background color set to " + hexColor));
                                                                } catch (Exception e) {
                                                                    context.getSource().sendFeedback(Text.literal("§cError setting background color: " + e.getMessage()));
                                                                }
                                                                return 1;
                                                            })
                                                    )
                                            )
                                            .then(ClientCommandManager.literal("text")
                                                    .then(ClientCommandManager.argument("hexcolor", StringArgumentType.word())
                                                            .executes(context -> {
                                                                try {
                                                                    String hexColor = StringArgumentType.getString(context, "hexcolor");
                                                                    int color = parseHexColor(hexColor);
                                                                    spotifyHUD.setColors(spotifyHUD.getBackgroundColor(), color);
                                                                    context.getSource().sendFeedback(Text.literal("§aSpotify HUD text color set to " + hexColor));
                                                                } catch (Exception e) {
                                                                    context.getSource().sendFeedback(Text.literal("§cError setting text color: " + e.getMessage()));
                                                                }
                                                                return 1;
                                                            })
                                                    )
                                            )
                                    )
                                    .then(ClientCommandManager.literal("scroll")
                                            .then(ClientCommandManager.literal("enable")
                                                    .executes(context -> {
                                                        try {
                                                            spotifyHUD.setEnableScrolling(true);
                                                            context.getSource().sendFeedback(Text.literal("§aSpotify HUD text scrolling enabled"));
                                                        } catch (Exception e) {
                                                            context.getSource().sendFeedback(Text.literal("§cError enabling scrolling: " + e.getMessage()));
                                                        }
                                                        return 1;
                                                    })
                                            )
                                            .then(ClientCommandManager.literal("disable")
                                                    .executes(context -> {
                                                        try {
                                                            spotifyHUD.setEnableScrolling(false);
                                                            context.getSource().sendFeedback(Text.literal("§aSpotify HUD text scrolling disabled"));
                                                        } catch (Exception e) {
                                                            context.getSource().sendFeedback(Text.literal("§cError disabling scrolling: " + e.getMessage()));
                                                        }
                                                        return 1;
                                                    })
                                            )
                                            .then(ClientCommandManager.literal("maxwidth")
                                                    .then(ClientCommandManager.argument("pixels", IntegerArgumentType.integer(50, 500))
                                                            .executes(context -> {
                                                                try {
                                                                    int width = IntegerArgumentType.getInteger(context, "pixels");
                                                                    spotifyHUD.setMaxTextWidth(width);
                                                                    context.getSource().sendFeedback(Text.literal("§aSpotify HUD max text width set to " + width + " pixels"));
                                                                } catch (Exception e) {
                                                                    context.getSource().sendFeedback(Text.literal("§cError setting max width: " + e.getMessage()));
                                                                }
                                                                return 1;
                                                            })
                                                    )
                                            )
                                            .then(ClientCommandManager.literal("speed")
                                                    .then(ClientCommandManager.argument("pixels", IntegerArgumentType.integer(1, 10))
                                                            .executes(context -> {
                                                                try {
                                                                    int speed = IntegerArgumentType.getInteger(context, "pixels");
                                                                    spotifyHUD.setScrollSpeed(speed);
                                                                    context.getSource().sendFeedback(Text.literal("§aSpotify HUD scroll speed set to " + speed + " pixels"));
                                                                } catch (Exception e) {
                                                                    context.getSource().sendFeedback(Text.literal("§cError setting scroll speed: " + e.getMessage()));
                                                                }
                                                                return 1;
                                                            })
                                                    )
                                            )
                                            .then(ClientCommandManager.literal("delay")
                                                    .then(ClientCommandManager.argument("milliseconds", IntegerArgumentType.integer(10, 1000))
                                                            .executes(context -> {
                                                                try {
                                                                    int delay = IntegerArgumentType.getInteger(context, "milliseconds");
                                                                    spotifyHUD.setScrollDelay(delay);
                                                                    context.getSource().sendFeedback(Text.literal("§aSpotify HUD scroll delay set to " + delay + " milliseconds"));
                                                                } catch (Exception e) {
                                                                    context.getSource().sendFeedback(Text.literal("§cError setting scroll delay: " + e.getMessage()));
                                                                }
                                                                return 1;
                                                            })
                                                    )
                                            )
                                    )
                                    .then(ClientCommandManager.literal("notplaying")
                                            .then(ClientCommandManager.literal("show")
                                                    .executes(context -> {
                                                        try {
                                                            spotifyHUD.setShowWhenNotPlaying(true);
                                                            context.getSource().sendFeedback(Text.literal("§aSpotify HUD will now show when nothing is playing"));
                                                        } catch (Exception e) {
                                                            context.getSource().sendFeedback(Text.literal("§cError: " + e.getMessage()));
                                                        }
                                                        return 1;
                                                    })
                                            )
                                            .then(ClientCommandManager.literal("hide")
                                                    .executes(context -> {
                                                        try {
                                                            spotifyHUD.setShowWhenNotPlaying(false);
                                                            context.getSource().sendFeedback(Text.literal("§aSpotify HUD will hide when nothing is playing"));
                                                        } catch (Exception e) {
                                                            context.getSource().sendFeedback(Text.literal("§cError: " + e.getMessage()));
                                                        }
                                                        return 1;
                                                    })
                                            )
                                    )
                            )
            );
        } catch (Exception e) {
            System.err.println("Error registering commands: " + e.getMessage());
        }
    }

    private int parseHexColor(String hexColor) {
        if (hexColor.startsWith("#")) {
            hexColor = hexColor.substring(1);
        } else if (hexColor.startsWith("0x")) {
            hexColor = hexColor.substring(2);
        }

        int color;
        switch (hexColor.length()) {
            case 3:
                String r = hexColor.substring(0, 1);
                String g = hexColor.substring(1, 2);
                String b = hexColor.substring(2, 3);
                color = Integer.parseInt(r + r + g + g + b + b, 16) | 0xFF000000;
                break;
            case 4:
                r = hexColor.substring(0, 1);
                g = hexColor.substring(1, 2);
                b = hexColor.substring(2, 3);
                String a = hexColor.substring(3, 4);
                color = Integer.parseInt(a + a + r + r + g + g + b + b, 16);
                break;
            case 6:
                color = Integer.parseInt(hexColor, 16) | 0xFF000000;
                break;
            case 8:
                String aa = hexColor.substring(0, 2);
                String rr = hexColor.substring(2, 4);
                String gg = hexColor.substring(4, 6);
                String bb = hexColor.substring(6, 8);
                color = Integer.parseInt(aa + rr + gg + bb, 16);
                break;
            default:
                throw new IllegalArgumentException("Invalid hex color format: " + hexColor);
        }

        return color;
    }
}