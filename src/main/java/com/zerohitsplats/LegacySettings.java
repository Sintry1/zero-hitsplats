package com.zerohitsplats;

import net.runelite.client.config.ConfigManager;

/** Preserves settings from the plugin's previous name on the first startup. */
final class LegacySettings
{
    static void migrate(ConfigManager manager)
    {
        if ("true".equals(manager.getConfiguration("zerohitsplats", "legacySettingsMigrated"))) { return; }
        for (String key : new String[] {"attachToPlayer", "offsetX", "offsetY", "speed", "distance", "iconSize", "fontSize", "fade", "preview"})
        {
            String value = manager.getConfiguration("zeroxpdrops", key);
            if (value != null) { manager.setConfiguration("zerohitsplats", key, value); }
        }
        for (String suffix : new String[] {"_preferredLocation", "_preferredPosition", "_preferredSize", "_origin", "_originX", "_originY"})
        {
            String value = manager.getConfiguration("runelite", "ZeroXpDropsOverlay" + suffix);
            if (value != null && manager.getConfiguration("runelite", "ZeroHitsplatsOverlay" + suffix) == null)
            {
                manager.setConfiguration("runelite", "ZeroHitsplatsOverlay" + suffix, value);
            }
        }
        manager.setConfiguration("zerohitsplats", "legacySettingsMigrated", true);
    }

    private LegacySettings() {}
}
