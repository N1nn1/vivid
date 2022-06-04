package com.ninni.vivid;

import com.google.common.reflect.Reflection;
import com.ninni.vivid.effects.VividStatusEffects;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class Vivid implements ModInitializer {
	public static final String MOD_ID = "vivid";
	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "item_group"), () -> new ItemStack(Items.APPLE));

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public void onInitialize() {
		Reflection.initialize();
		VividStatusEffects.init();
	}
}