package com.zerohitsplats;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.ui.overlay.OverlayPosition;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ZeroHitsplatsOverlayTest
{
    @Test public void idleOverlayHasDraggableBoundsAndPreservesSavedPosition()
    {
        Client client = mock(Client.class);
        when(client.getGameState()).thenReturn(GameState.LOGGED_IN);
        when(client.getViewportWidth()).thenReturn(800);
        ZeroHitsplatsOverlay overlay = new ZeroHitsplatsOverlay(client, new ZeroHitsplatsConfig() {});
        Graphics2D g = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB).createGraphics();
        try
        {
            Dimension bounds = overlay.render(g);
            assertTrue(overlay.isMovable());
            assertTrue(bounds.width > 0 && bounds.height > 120);
            overlay.setPreferredLocation(new Point(250, 200));
            overlay.getBounds().setLocation(250, 200);
            overlay.render(g);
            assertEquals(new Point(250, 200), overlay.getBounds().getLocation());
        }
        finally { g.dispose(); }
    }

    @Test public void iconScalesIndependentlyOfFont()
    {
        BufferedImage icon = new BufferedImage(12, 12, BufferedImage.TYPE_INT_ARGB);
        Graphics2D source = icon.createGraphics();
        source.setColor(Color.BLUE); source.fillRect(0, 0, 12, 12); source.dispose();
        assertEquals(16 * 16, bluePixels(icon, 16));
        assertEquals(48 * 48, bluePixels(icon, 48));
    }

    @Test public void snapAnchorPositionIsNotOverwrittenByDefaultOffsets()
    {
        Client client = mock(Client.class);
        when(client.getGameState()).thenReturn(GameState.LOGGED_IN);
        when(client.getViewportWidth()).thenReturn(800);
        ZeroHitsplatsOverlay overlay = new ZeroHitsplatsOverlay(client, new ZeroHitsplatsConfig() {});
        overlay.setPreferredPosition(OverlayPosition.TOP_RIGHT);
        overlay.getBounds().setLocation(700, 25); // Position supplied by RuneLite's anchor layout.
        Graphics2D g = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB).createGraphics();
        try
        {
            overlay.render(g);
            assertTrue(overlay.isSnappable());
            assertEquals(new Point(700, 25), overlay.getBounds().getLocation());
            overlay.getBounds().setLocation(600, 25); // Anchor moved after viewport resize.
            overlay.render(g);
            assertEquals(new Point(600, 25), overlay.getBounds().getLocation());
            overlay.setPreferredPosition(null); // Alt-right-click resets placement.
            overlay.render(g);
            assertNotEquals(new Point(600, 25), overlay.getBounds().getLocation());
        }
        finally { g.dispose(); }
    }

    private int bluePixels(BufferedImage icon, int size)
    {
        BufferedImage image = new BufferedImage(200, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setFont(new Font("Dialog", Font.PLAIN, 16));
        ZeroHitsplatsOverlay.drawDrop(g, icon, 100, 50, size);
        assertEquals(16, g.getFont().getSize());
        g.dispose();
        int count = 0;
        for (int x = 0; x < image.getWidth(); x++)
        {
            for (int y = 0; y < image.getHeight(); y++)
            {
                if (image.getRGB(x, y) == Color.BLUE.getRGB()) { count++; }
            }
        }
        return count;
    }
}
