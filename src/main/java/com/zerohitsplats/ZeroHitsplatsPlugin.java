package com.zerohitsplats;

import com.google.inject.Provides;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.Projectile;
import net.runelite.api.Skill;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.FakeXpDrop;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.StatChanged;
import net.runelite.api.gameval.SpriteID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
    name = "Zero Hitsplats",
    description = "Shows a scrolling blue hitsplat and zero when a melee or ranged attack gives no combat XP",
    tags = {"combat", "xp", "zero", "miss", "hitsplat"}
)
public class ZeroHitsplatsPlugin extends Plugin
{
    @Inject private Client client;
    @Inject private ZeroHitsplatsConfig config;
    @Inject private ZeroHitsplatsOverlay overlay;
    @Inject private OverlayManager overlayManager;
    @Inject private SpriteManager spriteManager;
    private final AttackBatch attacks = new AttackBatch();
    private final int[] xp = new int[Skill.values().length];
    private final Set<Projectile> seen = Collections.newSetFromMap(new IdentityHashMap<>());
    private boolean initialized;
    private long lastPreview;

    @Provides
    ZeroHitsplatsConfig provideConfig(ConfigManager manager) { return manager.getConfig(ZeroHitsplatsConfig.class); }

    @Override
    protected void startUp()
    {
        reset();
        overlayManager.add(overlay);
        // Baseline on the next game tick, avoiding startup XP being mistaken for a hit.
    }

    @Override
    protected void shutDown()
    {
        overlayManager.remove(overlay);
        reset();
    }

    private void reset()
    {
        initialized = false;
        attacks.clear();
        seen.clear();
        overlay.clear();
        lastPreview = 0;
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event)
    {
        if (event.getGameState() != GameState.LOGGED_IN) { reset(); }
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged event)
    {
        Player player = client.getLocalPlayer();
        if (!initialized || player == null || event.getActor() != player) { return; }
        if (AttackAnimations.contains(player.getAnimation()) && !CombatStyle.isMagic(client))
        {
            attacks.animation();
        }
    }

    @Subscribe
    public void onStatChanged(StatChanged event)
    {
        int index = event.getSkill().ordinal();
        int delta = event.getXp() - xp[index];
        xp[index] = event.getXp();
        if (initialized && delta > 0) { recordXp(event.getSkill()); }
    }

    @Subscribe
    public void onFakeXpDrop(FakeXpDrop event)
    {
        if (initialized && event.getXp() > 0) { recordXp(event.getSkill()); }
    }

    private void recordXp(Skill skill)
    {
        // Any combat XP suppresses a zero, including base XP from magic casts.
        if (skill == Skill.HITPOINTS || skill == Skill.ATTACK || skill == Skill.STRENGTH
            || skill == Skill.DEFENCE || skill == Skill.RANGED || skill == Skill.MAGIC)
        {
            attacks.damageXp();
        }
    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        if (client.getGameState() != GameState.LOGGED_IN || client.getLocalPlayer() == null) { return; }
        overlay.setSprite(spriteManager.getSprite(SpriteID.Hitmark.HITSPLAT_BLUE_MISS, 0));
        Set<Projectile> active = Collections.newSetFromMap(new IdentityHashMap<>());
        for (Projectile p : client.getProjectiles())
        {
            active.add(p);
            if (initialized && !seen.contains(p) && p.getSourceActor() == client.getLocalPlayer()
                && p.getTargetActor() != null && AttackAnimations.contains(client.getLocalPlayer().getAnimation())
                && !CombatStyle.isMagic(client))
            {
                attacks.projectile();
            }
        }
        seen.retainAll(active);
        seen.addAll(active);
        if (!initialized)
        {
            for (Skill skill : Skill.values()) { xp[skill.ordinal()] = client.getSkillExperience(skill); }
            attacks.clear();
            initialized = true;
            return;
        }
        if (attacks.finish())
        {
            overlay.addDrop();
        }
        long now = System.nanoTime();
        if (config.preview() && now - lastPreview > 2_000_000_000L)
        {
            overlay.addDrop();
            lastPreview = now;
        }
    }

}
