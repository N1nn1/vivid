package com.ninni.vivid.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;

import static com.ninni.vivid.Vivid.*;

public class VividEntities {
    public static final EntityType<PiglinVenatorEntity> PIGLIN_VENATOR = register(
        "piglin_venator",
        FabricEntityTypeBuilder.createMob()
                               .entityFactory(PiglinVenatorEntity::new)
                               .defaultAttributes(PiglinVenatorEntity::createPiglinVenatorAttributes)
                               .spawnGroup(SpawnGroup.MONSTER)
                               .spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.WORLD_SURFACE_WG, PiglinVenatorEntity::canSpawn)
                               .dimensions(EntityDimensions.changing(0.6F, 1.95F))
                               .trackRangeBlocks(8),
        new int[]{ 0x574038, 0x1e1e28 }
    );


    @SuppressWarnings("unchecked")
    private static <T extends Entity> EntityType<T> register(String id, EntityType<T> entityType, int[] spawnEggColors) {
        if (spawnEggColors != null)
            Registry.register(Registry.ITEM, new Identifier(MOD_ID, id + "_spawn_egg"), new SpawnEggItem((EntityType<? extends MobEntity>) entityType, spawnEggColors[0], spawnEggColors[1], new Item.Settings().maxCount(64).group(ItemGroup.MISC)));

        return Registry.register(Registry.ENTITY_TYPE, new Identifier(MOD_ID, id), entityType);
    }

    private static <T extends Entity> EntityType<T> register(String id, FabricEntityTypeBuilder<T> entityType, int[] spawnEggColors) {
        return register(id, entityType.build(), spawnEggColors);
    }

}


