package com.zerohitsplats;

import net.runelite.client.config.ConfigManager;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class LegacySettingsTest
{
    @Test public void copiesAppearanceAndAnchorWithoutOverwritingNewAnchor()
    {
        ConfigManager manager = mock(ConfigManager.class);
        when(manager.getConfiguration("zeroxpdrops", "iconSize")).thenReturn("40");
        when(manager.getConfiguration("runelite", "ZeroXpDropsOverlay_preferredPosition")).thenReturn("TOP_RIGHT");
        when(manager.getConfiguration("runelite", "ZeroXpDropsOverlay_preferredLocation")).thenReturn("100:200");
        when(manager.getConfiguration("runelite", "ZeroHitsplatsOverlay_preferredLocation")).thenReturn("300:400");
        LegacySettings.migrate(manager);
        verify(manager).setConfiguration("zerohitsplats", "iconSize", "40");
        verify(manager).setConfiguration("runelite", "ZeroHitsplatsOverlay_preferredPosition", "TOP_RIGHT");
        verify(manager, never()).setConfiguration("runelite", "ZeroHitsplatsOverlay_preferredLocation", "100:200");
        verify(manager).setConfiguration("zerohitsplats", "legacySettingsMigrated", true);
    }

    @Test public void migrationDoesNotRepeatAfterUserChangesSettings()
    {
        ConfigManager manager = mock(ConfigManager.class);
        when(manager.getConfiguration("zerohitsplats", "legacySettingsMigrated")).thenReturn("true");
        LegacySettings.migrate(manager);
        verify(manager).getConfiguration("zerohitsplats", "legacySettingsMigrated");
        verifyNoMoreInteractions(manager);
    }
}
