package com.zerohitsplats;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public final class ZeroHitsplatsLauncher
{
    public static void main(String[] args) throws Exception
    {
        ExternalPluginManager.loadBuiltin(ZeroHitsplatsPlugin.class);
        RuneLite.main(args);
    }
}
