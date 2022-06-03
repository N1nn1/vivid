package com.ninni.vivid.entity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PiglinBruteEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

import java.util.Random;

public class PiglinVenatorEntity extends PiglinBruteEntity {

    public PiglinVenatorEntity(EntityType<? extends PiglinBruteEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 20;
    }

    public static DefaultAttributeContainer.Builder createPiglinVenatorAttributes() {
        return createMobAttributes()
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35F)
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 35.0D)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0D);
    }

    @SuppressWarnings("unused")
    public static boolean canSpawn(EntityType<PiglinVenatorEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return !world.getBlockState(pos.down()).isOf(Blocks.NETHER_WART_BLOCK);
    }
}
