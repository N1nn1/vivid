package com.ninni.vivid;

import com.google.common.collect.ImmutableMap;
import com.ninni.vivid.client.init.VividEntityModelLayers;
import com.ninni.vivid.client.model.PiglinVenatorEntityModel;
import com.ninni.vivid.client.renderer.PiglinVenatorEntityRenderer;
import com.ninni.vivid.entity.VividEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class VividClient implements ClientModInitializer {

	@Override
	@SuppressWarnings({ "deprecation" })
	public void onInitializeClient() {
		EntityRendererRegistry erri = EntityRendererRegistry.INSTANCE;

		erri.register(VividEntities.PIGLIN_VENATOR, PiglinVenatorEntityRenderer::new);

		new ImmutableMap.Builder<EntityModelLayer, EntityModelLayerRegistry.TexturedModelDataProvider>()

			.put(VividEntityModelLayers.PIGLIN_VENATOR, PiglinVenatorEntityModel::getTexturedModelData)

			.build().forEach(EntityModelLayerRegistry::registerModelLayer);
	}
}