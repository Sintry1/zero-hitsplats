package com.zerohitsplats;

import net.runelite.client.config.*;

@ConfigGroup("zerohitsplats")
public interface ZeroHitsplatsConfig extends Config
{
    @ConfigItem(keyName = "attachToPlayer", name = "Attach to player", description = "Scroll above your player instead of the viewport center", position = 0)
    default boolean attachToPlayer() { return false; }

    @Range(min = -2000, max = 2000)
    @ConfigItem(keyName = "offsetX", name = "Default horizontal offset", description = "Initial position before Alt-drag, or offset when attached to player", position = 1)
    default int offsetX() { return 0; }

    @Range(min = -2000, max = 2000)
    @ConfigItem(keyName = "offsetY", name = "Default vertical offset", description = "Initial position before Alt-drag, or offset when attached to player", position = 2)
    default int offsetY() { return 180; }

    @Range(min = 10, max = 300)
    @ConfigItem(keyName = "speed", name = "Scroll speed", description = "Upward movement in pixels per second", position = 3)
    default int speed() { return 60; }

    @Range(min = 30, max = 600)
    @ConfigItem(keyName = "distance", name = "Scroll distance", description = "Distance before disappearing", position = 4)
    default int distance() { return 120; }

    @Range(min = 8, max = 96)
    @ConfigItem(keyName = "iconSize", name = "Hitsplat icon size", description = "Blue icon width in pixels, independent of Font size", position = 5)
    default int iconSize() { return 24; }

    @Range(min = 12, max = 48)
    @ConfigItem(keyName = "fontSize", name = "Font size", description = "Size of the regular RuneScape text; 16 matches the default RuneScape XP-drop font", position = 6)
    default int fontSize() { return 16; }

    @ConfigItem(keyName = "fade", name = "Fade out", description = "Fade during the final quarter of the drop", position = 7)
    default boolean fade() { return true; }

    @ConfigItem(keyName = "preview", name = "Preview", description = "Show a sample zero every two seconds while logged in", position = 8)
    default boolean preview() { return false; }

}
