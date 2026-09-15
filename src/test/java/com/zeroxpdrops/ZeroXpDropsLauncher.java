package com.zeroxpdrops;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public final class ZeroXpDropsLauncher
{
    public static void main(String[] args) throws Exception
    {
        ExternalPluginManager.loadBuiltin(ZeroXpDropsPlugin.class);
        RuneLite.main(args);
    }
}
