package com.ninni.vivid.client.renderer;


import com.ninni.vivid.client.init.VividEntityModelLayers;
import com.ninni.vivid.client.model.PiglinVenatorEntityModel;
import com.ninni.vivid.entity.PiglinVenatorEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

import static com.ninni.vivid.Vivid.*;

@Environment(EnvType.CLIENT)
public class PiglinVenatorEntityRenderer extends MobEntityRenderer<PiglinVenatorEntity, PiglinVenatorEntityModel> {
    public static final Identifier TEXTURE = new Identifier(MOD_ID, "textures/entity/piglin/piglin_venator.png");

    public PiglinVenatorEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new PiglinVenatorEntityModel(ctx.getPart(VividEntityModelLayers.PIGLIN_VENATOR)), 0.5F);
    }

    @Override
    protected boolean isShaking(PiglinVenatorEntity piglin) {
        return super.isShaking(piglin) || (piglin).shouldZombify();
    }

    @Override
    public Identifier getTexture(PiglinVenatorEntity entity) { return TEXTURE; }
}

