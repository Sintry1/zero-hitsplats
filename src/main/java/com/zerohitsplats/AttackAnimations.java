/*
 * Copyright (c) 2021, Matsyir <https://github.com/matsyir>
 * Copyright (c) 2020, Mazhar <https://twitter.com/maz_rs>
 * Copyright (c) 2024-2026, Lexer747 <https://github.com/Lexer747>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.zerohitsplats;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import net.runelite.api.gameval.AnimationID;

// Attack animation data adapted from ngraves95/attacktimer AnimationData.java.
final class AttackAnimations
{
    private static final Set<Integer> IDS = new HashSet<>(Arrays.asList(
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
        AnimationID.ROSEWOOD_BLOWPIPE_SPECIAL_ATTACK,
        AnimationID.ABYSSAL_BLUDGEON_CRUSH,
        AnimationID.ABYSSAL_BLUDGEON_SPECIAL_ATTACK,
        AnimationID.ABYSSAL_DAGGER_LUNGE,
        AnimationID.AGS_SPECIAL_ORNATE_PLAYER,
        AnimationID.AGS_SPECIAL_PLAYER,
        AnimationID.ANCIENT_AXE_CRUSH,
        AnimationID.ANCIENT_AXE_SPECIAL,
        AnimationID.II_HUMAN_DART_THROW,
        AnimationID.DTTD_PLAYER_FIRE_BONE_CROSSBOW,
        AnimationID.HUMAN_GLAIVE_RALOS01_UNCHARGED_THROW,
        AnimationID.HUMAN_GLAIVE_RALOS01_UNCHARGED_SPECIAL,
        AnimationID.BALLISTA_ATTACK,
        AnimationID.BALLISTA_ATTACK_PVN,
        AnimationID.BALLISTA_SPECIAL_ATTACK_PVN,
        AnimationID.BARROW_DHAROK_CRUSH,
        AnimationID.BARROW_DHAROK_SLASH,
        AnimationID.BARROW_GUTHAN_CRUSH,
        AnimationID.BARROW_TORAG_CRUSH,
        AnimationID.BARROWS_QUARTERSTAFF_ATTACK,
        AnimationID.BARROWS_REPEATING_CROSSBOW_FIRE,
        AnimationID.BARROWS_WAR_SPEAR_CRUSH,
        AnimationID.BARROWS_WAR_SPEAR_SLASH,
        AnimationID.BARROWS_WAR_SPEAR_STAB,
        AnimationID.BATTLEAXE_CRUSH,
        AnimationID.BGS_SPECIAL_ORNATE_PLAYER,
        AnimationID.BGS_SPECIAL_PLAYER,
        AnimationID.BRAIN_PLAYER_ANCHOR_ATTACK,
        AnimationID.BRAIN_PLAYER_ANCHOR_SPECIAL_ATTACK,
        AnimationID.CHAINHIT,
        AnimationID.CLEAVE,
        AnimationID.DARK_SPEC_PLAYER,
        AnimationID.DH_SWORD_UPDATE_BLOCK,
        AnimationID.DH_SWORD_UPDATE_SLASH,
        AnimationID.DH_SWORD_UPDATE_SMASH,
        AnimationID.DRAGON_HALBERD_SPECIAL_ATTACK,
        AnimationID.DRAGON_PICKAXE_ANIM,
        AnimationID.DRAGON_TWO_HANDED_SWORD,
        AnimationID.DRAGON_WARHAMMER_SA_PLAYER,
        AnimationID.DTTD_PLAYER_FIRE_BONE_CROSSBOW_PVN,
        AnimationID.DTTD_PLAYER_STAB_BONE_DAGGER,
        AnimationID.FORESTRY_2H_AXE_ATTACK,
        AnimationID.GHRAZI_RAPIER_ATTACK,
        AnimationID.GODWARS_GODSWORD_ZAMORAK_PLAYER,
        AnimationID.HUMAN_ATLATL_ATTACK_RANGED_01,
        AnimationID.HUMAN_ATTACK_SALAMANDER,
        AnimationID.HUMAN_AXE_CHOP,
        AnimationID.HUMAN_AXE_HACK,
        AnimationID.HUMAN_BLUNT_POUND,
        AnimationID.HUMAN_BLUNT_SPIKE,
        AnimationID.HUMAN_BOW,
        AnimationID.HUMAN_CHINCHOMPA_ATTACK_PVN,
        AnimationID.HUMAN_DDAGGER_LUNGE,
        AnimationID.HUMAN_DHSWORD_CHOP,
        AnimationID.HUMAN_DHSWORD_SLASH,
        AnimationID.HUMAN_DHUNTER_LANCE_ATTACK,
        AnimationID.HUMAN_DHUNTER_LANCE_CRUSH,
        AnimationID.HUMAN_DHUNTER_LANCE_SLASH,
        AnimationID.HUMAN_DINHS_BULWARK_BASH,
        AnimationID.HUMAN_DRAGON_CLAWS_SPEC,
        AnimationID.HUMAN_DRAGON_KNIFE,
        AnimationID.HUMAN_DRAGON_KNIFE_P,
        AnimationID.HUMAN_DRAGON_SWORD_SPEC,
        AnimationID.HUMAN_DRAGON_TAXE_SPEC,
        AnimationID.HUMAN_DRAGON_TKNIVES_SPEC,
        AnimationID.HUMAN_DRAGON_TKNIVES_SPEC_POISON,
        AnimationID.HUMAN_DSPEAR_STAB,
        AnimationID.HUMAN_ELDER_MAUL_ATTACK,
        AnimationID.HUMAN_ELDER_MAUL_SPEC,
        AnimationID.HUMAN_GLAIVE_RALOS01_CHARGED_SPECIAL,
        AnimationID.HUMAN_GLAIVE_RALOS01_CHARGED_THROW,
        AnimationID.HUMAN_HALLOWFELL_SLASH,
        AnimationID.HUMAN_INQUISITORS_MACE_CRUSH,
        AnimationID.HUMAN_OSMUMTENS_FANG,
        AnimationID.HUMAN_SCYTHE_SWEEP,
        AnimationID.HUMAN_SPEAR_LUNGE,
        AnimationID.HUMAN_SPEAR_SPIKE,
        AnimationID.HUMAN_SPECIAL_ATLATL_01,
        AnimationID.HUMAN_SPECIAL01_WEBWEAVER,
        AnimationID.HUMAN_SPECIAL02_URSINE,
        AnimationID.HUMAN_STAFF_PUMMEL,
        AnimationID.HUMAN_STAFFORB_PUMMEL,
        AnimationID.HUMAN_STAKE2,
        AnimationID.HUMAN_STAKE2_PVN,
        AnimationID.HUMAN_SWORD_SLASH,
        AnimationID.HUMAN_SWORD_STAB,
        AnimationID.HUMAN_UNARMEDKICK,
        AnimationID.HUMAN_UNARMEDPUNCH,
        AnimationID.HUMAN_WEAPON_BOW_SCORCHED_01_SPEC,
        AnimationID.HUMAN_WEAPON_BOW_VENATOR01_SHOOT,
        AnimationID.HUMAN_WEAPONS_HALLOWED_FLAIL01_ATTACK01,
        AnimationID.HUMAN_XBOWS_LEAGUE03_ATTACK_PVN,
        AnimationID.HUMAN_ZAMORAKSPEAR_LUNGE,
        AnimationID.HUMAN_ZAMORAKSPEAR_STAB,
        AnimationID.II_HUMAN_DART_THROW_PVN,
        AnimationID.IMPALE,
        AnimationID.IVANDIS_FLAIL_ATTACK,
        AnimationID.NGS_SPECIAL_PLAYER,
        AnimationID.OLAF2_BRINE_SABRE_SPECIAL,
        AnimationID.PMOON_MACUAHUITL_CRUSH,
        AnimationID.PUNCTURE,
        AnimationID.SARADOMIN_SWORD_SPECIAL_PLAYER,
        AnimationID.SCYTHE_OF_VITUR_ATTACK,
        AnimationID.SGS_SPECIAL_ORNATE_PLAYER,
        AnimationID.SGS_SPECIAL_PLAYER,
        AnimationID.SHATTER,
        AnimationID.SLAYER_ABYSSAL_WHIP_ATTACK,
        AnimationID.SLAYER_GRANITE_MAUL_ATTACK,
        AnimationID.SLAYER_GRANITE_MAUL_SPECIAL_ATTACK,
        AnimationID.SLICE_PLAYER_MACE_SPECIAL_ATTACK,
        AnimationID.SNAKEBOSS_BLOWPIPE_ATTACK,
        AnimationID.SNAKEBOSS_BLOWPIPE_ATTACK_ORNAMENT,
        AnimationID.SNAPSHOT,
        AnimationID.SP_ATTACK_DRAGON_SCIMITAR,
        AnimationID.TECPATL_STAB,
        AnimationID.WILD_CAVE_CHAINMACE_CRUSH,
        AnimationID.XBOWS_HUMAN_FIRE_AND_RELOAD,
        AnimationID.XBOWS_HUMAN_FIRE_AND_RELOAD_PVN,
        AnimationID.ZCB_ATTACK,
        AnimationID.ZCB_ATTACK_PVN,
        AnimationID.ZGS_SPECIAL_ORNATE_PLAYER,
        AnimationID.ZGS_SPECIAL_PLAYER
    ));
    static boolean contains(int id) { return IDS.contains(id); }
    private AttackAnimations() {}
}
