package com.ninni.vivid.mixin.client;

import com.ninni.vivid.Vivid;
import com.ninni.vivid.api.ICaptive;
import net.minecraft.client.render.entity.HoglinEntityRenderer;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoglinEntityRenderer.class)
public class HoglinEntityRendererMixin {
    private static final Identifier CAPTIVE_TEXTURE = new Identifier(Vivid.MOD_ID, "textures/entity/hoglin/captive_hoglin.png");

    @Inject(at = @At("HEAD"), method = "getTexture(Lnet/minecraft/entity/mob/HoglinEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
    private void getTexture(HoglinEntity hoglinEntity, CallbackInfoReturnable<Identifier> cir) {
        if (((ICaptive)hoglinEntity).isCaptive()) {
            cir.setReturnValue(CAPTIVE_TEXTURE);
        }
    }

}
