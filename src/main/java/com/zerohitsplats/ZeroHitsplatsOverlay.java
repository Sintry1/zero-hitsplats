package com.zerohitsplats;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class ZeroHitsplatsOverlay extends Overlay
{
    private final Client client;
    private final ZeroHitsplatsConfig config;
    private final ArrayDeque<Long> drops = new ArrayDeque<>();
    private BufferedImage sprite;
    // Derived font, re-derived only when the configured size changes.
    private Font cachedFont;
    private int cachedFontSize = -1;

    @Inject
    ZeroHitsplatsOverlay(Client client, ZeroHitsplatsConfig config)
    {
        this.client = client;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setMovable(true);
        setSnappable(true);
        setLayer(OverlayLayer.ABOVE_SCENE);
        setPriority(Overlay.PRIORITY_HIGH);
    }

    void setSprite(BufferedImage sprite) { this.sprite = sprite; }
    void clear() { drops.clear(); }
    void addDrop()
    {
        long start = System.nanoTime();
        // Space consecutive drops without making their motion depend on FPS.
        if (!drops.isEmpty())
        {
            long gap = (long) ((Math.max(config.iconSize(), config.fontSize()) + 6) * 1_000_000_000.0 / config.speed());
            start = Math.max(start, drops.peekLast() + gap);
        }
        if (drops.size() >= 32) { drops.removeFirst(); }
        drops.addLast(start);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        // Config getters are not field reads — each parses a String — so snapshot the ones used
        // repeatedly below. They cannot meaningfully change within a single frame.
        final int distance = config.distance();
        final int speed = config.speed();
        final int iconSize = config.iconSize();
        final int fontSize = config.fontSize();
        final boolean attachToPlayer = config.attachToPlayer();
        long now = System.nanoTime();
        double lifetime = distance / (double) speed;
        while (!drops.isEmpty() && (now - drops.peekFirst()) / 1e9 >= lifetime) { drops.removeFirst(); }
        if (client.getGameState() != GameState.LOGGED_IN) { return null; }
        if (cachedFont == null || cachedFontSize != fontSize)
        {
            cachedFont = FontManager.getRunescapeFont().deriveFont(Font.PLAIN, (float) fontSize);
            cachedFontSize = fontSize;
        }
        graphics.setFont(cachedFont);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        int iconHeight = sprite == null ? iconSize
            : Math.max(1, sprite.getHeight() * iconSize / sprite.getWidth());
        int rowHeight = Math.max(iconHeight, graphics.getFontMetrics().getHeight()) + 8;
        Dimension area = new Dimension(iconSize + 5 + graphics.getFontMetrics().stringWidth("0") + 8,
            distance + rowHeight);
        setMovable(!attachToPlayer);
        setSnappable(!attachToPlayer);
        int x = client.getViewportXOffset() + client.getViewportWidth() / 2;
        int y = client.getViewportYOffset();
        if (attachToPlayer)
        {
            Player player = client.getLocalPlayer();
            Point point = player == null ? null : player.getCanvasTextLocation(graphics, "", player.getLogicalHeight() + 40);
            if (point == null) { return null; }
            x = point.getX();
            y = point.getY();
        }
        x += config.offsetX();
        y += config.offsetY();
        Graphics2D g = (Graphics2D) graphics.create();
        try
        {
            // RuneLite translates graphics to the persisted Alt-drag position.
            // Before the first drag, retain the old viewport/player placement.
            if (attachToPlayer || (getPreferredLocation() == null && getPreferredPosition() == null))
            {
                int left = x - area.width / 2;
                int top = y - distance - rowHeight / 2;
                g.translate(left - getBounds().x, top - getBounds().y);
                getBounds().setLocation(left, top);
            }
            x = area.width / 2;
            y = distance + rowHeight / 2;
            // Keep non-empty bounds while idle so holding Alt always reveals the handle.
            if (sprite == null) { return area; }
            final boolean fade = config.fade();
            for (long start : drops)
            {
                double age = (now - start) / 1e9;
                if (age < 0) { continue; }
                float alpha = fade ? (float) Math.min(1, (lifetime - age) / (lifetime * 0.25)) : 1;
                g.setComposite(AlphaComposite.SrcOver.derive(Math.max(0, alpha)));
                drawDrop(g, sprite, x, y - (int) (age * speed), iconSize);
            }
        }
        finally { g.dispose(); }
        return area;
    }

    static void drawDrop(Graphics2D g, BufferedImage sprite, int centerX, int centerY, int size)
    {
        FontMetrics metrics = g.getFontMetrics();
        int width = size + 5 + metrics.stringWidth("0");
        int left = centerX - width / 2;
        int height = Math.max(1, sprite.getHeight() * size / sprite.getWidth());
        g.drawImage(sprite, left, centerY - height / 2, size, height, null);
        int baseline = centerY + (metrics.getAscent() - metrics.getDescent()) / 2;
        g.setColor(Color.BLACK);
        g.drawString("0", left + size + 6, baseline + 1);
        g.setColor(Color.WHITE);
        g.drawString("0", left + size + 5, baseline);
    }
}
