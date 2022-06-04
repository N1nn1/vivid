package com.ninni.vivid.entity.ai;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;

public class VenatorForgetAngryTask extends Task<MobEntity> {

    public VenatorForgetAngryTask() {
        super(ImmutableMap.of(MemoryModuleType.ANGRY_AT, MemoryModuleState.VALUE_PRESENT));
    }

    @Override
    protected void run(ServerWorld world, MobEntity mobEntity, long time) {
        LookTargetUtil.getEntity(mobEntity, MemoryModuleType.ANGRY_AT).ifPresent(target -> {
            if (target.isDead() && (target.getType() != EntityType.PLAYER || world.getGameRules().getBoolean(GameRules.FORGIVE_DEAD_PLAYERS))) {
                mobEntity.getBrain().forget(MemoryModuleType.ANGRY_AT);
            }
        });
    }
}
