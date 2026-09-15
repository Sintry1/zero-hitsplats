package com.zerohitsplats;

import java.lang.reflect.Field;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.Deque;
import net.runelite.api.EnumComposition;
import net.runelite.api.EnumID;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.ParamID;
import net.runelite.api.Player;
import net.runelite.api.Projectile;
import net.runelite.api.Skill;
import net.runelite.api.StructComposition;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.FakeXpDrop;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.StatChanged;
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

    @Test public void magicSplashWithBaseXpDoesNotShowZero()
    {
        animate(AnimationID.HUMAN_CASTSTRIKE_STAFF);
        xp(Skill.MAGIC, 1005);
        tick();
        verify(overlay, never()).addDrop();
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
    private void selectedStyle(String name)
    {
        EnumComposition weapons = mock(EnumComposition.class);
        EnumComposition styles = mock(EnumComposition.class);
        StructComposition style = mock(StructComposition.class);
        when(client.getEnum(EnumID.WEAPON_STYLES)).thenReturn(weapons);
        when(weapons.getIntValue(anyInt())).thenReturn(123456);
        when(client.getEnum(123456)).thenReturn(styles);
        when(styles.getIntVals()).thenReturn(new int[]{123});
        when(client.getStructComposition(123)).thenReturn(style);
        when(style.getStringValue(ParamID.ATTACK_STYLE_NAME)).thenReturn(name);
    }

    @Test public void rangedFamiliesShowMissesButNotSuccessfulHits() throws Exception
    {
        int[] animations = {
            AnimationID.HUMAN_BOW, AnimationID.SNAPSHOT,
            AnimationID.XBOWS_HUMAN_FIRE_AND_RELOAD_PVN, AnimationID.ZCB_ATTACK,
            AnimationID.DTTD_PLAYER_FIRE_BONE_CROSSBOW,
            AnimationID.BARROWS_REPEATING_CROSSBOW_FIRE,
            AnimationID.SNAKEBOSS_BLOWPIPE_ATTACK, AnimationID.SNAKEBOSS_BLOWPIPE_ATTACK_ORNAMENT,
            AnimationID.II_HUMAN_DART_THROW, AnimationID.II_HUMAN_DART_THROW_PVN,
            AnimationID.HUMAN_DRAGON_KNIFE, AnimationID.HUMAN_STAKE2_PVN,
            AnimationID.CHAINHIT, AnimationID.HUMAN_CHINCHOMPA_ATTACK_PVN,
            AnimationID.BALLISTA_ATTACK, AnimationID.BALLISTA_ATTACK_PVN,
            AnimationID.HUMAN_ATLATL_ATTACK_RANGED_01,
            AnimationID.HUMAN_WEAPON_BOW_VENATOR01_SHOOT,
            AnimationID.HUMAN_GLAIVE_RALOS01_UNCHARGED_THROW,
            AnimationID.HUMAN_GLAIVE_RALOS01_UNCHARGED_SPECIAL,
            AnimationID.HUMAN_GLAIVE_RALOS01_CHARGED_THROW
        };
        for (int animation : animations)
        {
            setup();
            animate(animation);
            projectiles(projectile(player));
            tick(); tick();
            verify(overlay, times(1)).addDrop();
            setup();
            animate(animation);
            projectiles(projectile(player));
            xp(Skill.RANGED, 1040);
            tick(); tick();
            verify(overlay, never()).addDrop();
        }
    }

    @Test public void salamanderRangedMissShowsZero()
    {
        selectedStyle("Ranging");
        animate(AnimationID.HUMAN_ATTACK_SALAMANDER);
        projectiles(projectile(player));
        tick();
        verify(overlay).addDrop();
    }

    @Test public void salamanderMagicMissDoesNotShowZero()
    {
        selectedStyle("Casting");
        animate(AnimationID.HUMAN_ATTACK_SALAMANDER);
        projectiles(projectile(player));
        tick();
        verify(overlay, never()).addDrop();
    }

    @Test public void poweredStaffSharedAnimationDoesNotShowZero()
    {
        selectedStyle("Casting");
        animate(AnimationID.HUMAN_AXE_CHOP);
        projectiles(projectile(player));
        tick();
        verify(overlay, never()).addDrop();
    }

    @Test public void magicAnimationsWithoutXpDoNotShowZero() throws Exception
    {
        for (int animation : new int[]{AnimationID.HUMAN_CASTSTRIKE_STAFF,
            AnimationID.POG_WARPED_SCEPTRE_ATTACK, AnimationID.TOA_SOT_CAST_B})
        {
            setup();
            animate(animation);
            projectiles(projectile(player));
            tick(); tick();
            verify(overlay, never()).addDrop();
        }
    }

    @Test public void magicXpSuppressesLingeringRangedAnimation()
    {
        when(player.getAnimation()).thenReturn(AnimationID.SNAKEBOSS_BLOWPIPE_ATTACK);
        projectiles(projectile(player));
        xp(Skill.MAGIC, 1005);
        tick();
        verify(overlay, never()).addDrop();
    }
    @Test public void noxiousAndSpecialAttacksShowOnlyMisses() throws Exception
    {
        int[] animations = {
        AnimationID.ABYSSAL_DAGGER_SPECIAL,
        AnimationID.BLESSED_SARADOMIN_SWORD_SPECIAL_PLAYER,
        AnimationID.ARMADYL_SPECIAL_ATTACK,
        AnimationID.DH_SWORD_UPDATE_DRAGON_SPECIAL_PLAYER,
        AnimationID.HUMAN_INFERNAL_TECPATL_SPEC,
        AnimationID.HUMAN_KARAMBIT_SPEC,
        AnimationID.HUMAN_HALBERD_VIRULENCE_01,
        AnimationID.HUMAN_HALBERD_VIRULENCE_02,
        AnimationID.HUMAN_HALBERD_VIRULENCE_04,
        AnimationID.TOXIC_BLOWPIPE_SPECIAL_UPDATED,
        AnimationID.BALLISTA_SPECIAL_ATTACK,
        AnimationID.WEAPON_MORRIGANS_JAVELIN_SPECIAL01,
        AnimationID.WEAPON_MORRIGANS_THROWINGAXE_SPECIAL01,
        AnimationID.HUMAN_WEAPON_EMBERLIGHT_01_SPEC,
        AnimationID.HUMAN_WEAPON_BURNING_CLAWS_02_SPEC,
        AnimationID.WEAPON_SWORD_OSMUMTEN03_SPECIAL,
        AnimationID.HUMAN_SPECIAL_KHOPESH,
        AnimationID.ROSEWOOD_BLOWPIPE_SPECIAL_ATTACK
        };
        for (int animation : animations)
        {
            setup();
            animate(animation);
            tick(); tick();
            verify(overlay, times(1)).addDrop();
            setup();
            animate(animation);
            xp(Skill.HITPOINTS, 1013);
            tick(); tick();
            verify(overlay, never()).addDrop();
        }
    }

    @Test public void rangedSpecialWithSeveralProjectilesProducesOneDrop()
    {
        animate(AnimationID.HUMAN_SPECIAL01_WEBWEAVER);
        projectiles(projectile(player), projectile(player), projectile(player), projectile(player));
        tick(); tick();
        verify(overlay, times(1)).addDrop();
    }
}
