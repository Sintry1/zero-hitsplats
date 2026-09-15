package com.zerohitsplats;

import java.lang.reflect.Field;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.gameval.AnimationID;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.overlay.OverlayManager;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ZeroHitsplatsPluginTest
{
    private ZeroHitsplatsPlugin plugin;
    private Client client;
    private Player player;
    private ZeroHitsplatsOverlay overlay;

    @Before public void setup() throws Exception
    {
        plugin = new ZeroHitsplatsPlugin();
        client = mock(Client.class);
        player = mock(Player.class);
        overlay = mock(ZeroHitsplatsOverlay.class);
        put("client", client);
        put("configManager", mock(net.runelite.client.config.ConfigManager.class));
        put("config", mock(ZeroHitsplatsConfig.class));
        put("overlay", overlay);
        put("overlayManager", mock(OverlayManager.class));
        put("spriteManager", mock(SpriteManager.class));
        when(client.getLocalPlayer()).thenReturn(player);
        when(client.getGameState()).thenReturn(GameState.LOGGED_IN);
        when(client.getProjectiles()).thenReturn(mock(Deque.class));
        when(client.getProjectiles().iterator()).thenAnswer(call -> java.util.Collections.emptyIterator());
        when(client.getSkillExperience(any())).thenReturn(1000);
        plugin.startUp();
        tick();
        clearInvocations(overlay);
    }

    private void put(String name, Object value) throws Exception
    {
        Field field = ZeroHitsplatsPlugin.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(plugin, value);
    }

    private void animate(int animation)
    {
        when(player.getAnimation()).thenReturn(animation);
        AnimationChanged event = new AnimationChanged();
        event.setActor(player);
        plugin.onAnimationChanged(event);
    }

    private void xp(Skill skill, int amount)
    {
        plugin.onStatChanged(new StatChanged(skill, amount, 10, 10));
    }

    private void tick() { plugin.onGameTick(new GameTick()); }

    @Test public void meleeMissShowsZero()
    {
        animate(AnimationID.HUMAN_SWORD_SLASH);
        tick(); tick();
        verify(overlay, times(1)).addDrop();
    }

    @Test public void successfulMeleePreservesExistingXpDrops()
    {
        animate(AnimationID.HUMAN_SWORD_SLASH);
        xp(Skill.STRENGTH, 1040); xp(Skill.HITPOINTS, 1013);
        tick();
        verify(overlay, never()).addDrop();
    }

    @Test public void magicSplashWithBaseXpShowsZero()
    {
        animate(AnimationID.HUMAN_CASTSTRIKE_STAFF);
        xp(Skill.MAGIC, 1005);
        tick();
        verify(overlay).addDrop();
    }

    @Test public void damagingSpellDoesNotShowZero()
    {
        xp(Skill.HITPOINTS, 1013);
        animate(AnimationID.HUMAN_CASTSTRIKE_STAFF);
        xp(Skill.MAGIC, 1025);
        tick();
        verify(overlay, never()).addDrop();
    }

    @Test public void blockedXpStillCountsAsDamage()
    {
        animate(AnimationID.HUMAN_BOW);
        plugin.onFakeXpDrop(new FakeXpDrop(Skill.RANGED, 40));
        tick();
        verify(overlay, never()).addDrop();
    }

    @Test public void statBoostAndUnrelatedXpDoNotHideMiss()
    {
        animate(AnimationID.HUMAN_SWORD_SLASH);
        xp(Skill.STRENGTH, 1000);
        xp(Skill.WOODCUTTING, 1025);
        tick();
        verify(overlay).addDrop();
    }

    @Test public void eatingAndOtherPlayersDoNotProduceZeros()
    {
        animate(AnimationID.HUMAN_EAT);
        AnimationChanged event = new AnimationChanged();
        event.setActor(mock(Player.class));
        plugin.onAnimationChanged(event);
        tick();
        verify(overlay, never()).addDrop();
    }

    private Projectile projectile(Actor source)
    {
        Projectile projectile = mock(Projectile.class);
        when(projectile.getSourceActor()).thenReturn(source);
        when(projectile.getTargetActor()).thenReturn(mock(NPC.class));
        return projectile;
    }

    private void projectiles(Projectile... projectiles)
    {
        when(client.getProjectiles().iterator()).thenAnswer(call -> java.util.Arrays.asList(projectiles).iterator());
    }

    @Test public void retainedProjectileNeverRepeatsAndNewShotDoes()
    {
        when(player.getAnimation()).thenReturn(AnimationID.SNAKEBOSS_BLOWPIPE_ATTACK);
        Projectile first = projectile(player);
        projectiles(first); tick(); tick();
        Projectile second = projectile(player);
        projectiles(first, second); tick(); tick();
        verify(overlay, times(2)).addDrop();
    }

    @Test public void projectileFromAnotherActorIsIgnored()
    {
        when(player.getAnimation()).thenReturn(AnimationID.SNAKEBOSS_BLOWPIPE_ATTACK);
        projectiles(projectile(mock(Player.class))); tick();
        verify(overlay, never()).addDrop();
    }

    @Test public void animationAndProjectileInSameTickProduceOneZero()
    {
        animate(AnimationID.HUMAN_BOW);
        projectiles(projectile(player)); tick(); tick();
        verify(overlay, times(1)).addDrop();
    }

    @Test public void projectileAlreadyPresentOnLoginIsNotANewAttack()
    {
        plugin.shutDown();
        projectiles(projectile(player));
        when(player.getAnimation()).thenReturn(AnimationID.HUMAN_BOW);
        plugin.startUp(); tick(); tick();
        verify(overlay, never()).addDrop();
    }
}
