package com.ninni.vivid.client.init;

import com.ninni.vivid.mixin.client.EntityModelLayersInvoker;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import static com.ninni.vivid.Vivid.*;

public class VividEntityModelLayers {

    public static final EntityModelLayer PIGLIN_VENATOR = registerMain("piglin_venator");

    private static EntityModelLayer registerMain(String id) {
        return EntityModelLayersInvoker.register(new Identifier(MOD_ID, id).toString(), "main");
    }

    private static EntityModelLayer register(String id, String layer) {
        return EntityModelLayersInvoker.register(new Identifier(MOD_ID, id).toString(), layer);
    }
}

