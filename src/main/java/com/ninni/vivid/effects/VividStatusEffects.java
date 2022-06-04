package com.ninni.vivid.effects;

import com.ninni.vivid.Vivid;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class VividStatusEffects {

    public static final HoglinBlessingEffect HOGLIN_BLESSING = new HoglinBlessingEffect();

    public static void init() {
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Vivid.MOD_ID, "hoglin_blessing"), HOGLIN_BLESSING);
    }

}
