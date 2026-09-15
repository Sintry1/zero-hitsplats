package com.zerohitsplats;

import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.EnumID;
import net.runelite.api.ParamID;
import net.runelite.api.StructComposition;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.api.gameval.VarbitID;

final class CombatStyle
{
    static boolean isMagic(Client client)
    {
        int index = client.getVarpValue(VarPlayerID.COM_MODE);
        // Standard staff casting is style 4; defensive casting is its variant.
        if (index >= 4) { return true; }
        EnumComposition weapons = client.getEnum(EnumID.WEAPON_STYLES);
        if (weapons == null) { return false; }
        int enumId = weapons.getIntValue(client.getVarbitValue(VarbitID.COMBAT_WEAPON_CATEGORY));
        if (enumId < 0) { return false; }
        EnumComposition styles = client.getEnum(enumId);
        if (styles == null) { return false; }
        int[] ids = styles.getIntVals();
        if (ids == null || index < 0 || index >= ids.length) { return false; }
        StructComposition style = client.getStructComposition(ids[index]);
        if (style == null) { return false; }
        String name = style.getStringValue(ParamID.ATTACK_STYLE_NAME);
        return "Casting".equalsIgnoreCase(name) || "Defensive Casting".equalsIgnoreCase(name);
    }

    private CombatStyle() {}
}
