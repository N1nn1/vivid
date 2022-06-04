package com.ninni.vivid.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.PiglinBrain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PiglinBrain.class)
public interface PiglinBrainAccessor {
    @Invoker
    static void callTryRevenge(AbstractPiglinEntity piglin, LivingEntity target) {
        throw new UnsupportedOperationException();
    }
}
