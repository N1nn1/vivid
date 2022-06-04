package com.ninni.vivid.entity.ai;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;

//The other crossbow attack task uses generics which makes it incompatible with the setTaskList method. If you check the hoglin AI brain, it'll show an error 
public class NonGenericCrossbowAttackTask extends Task<MobEntity> {
    private int chargingCooldown;
    private NonGenericCrossbowAttackTask.CrossbowState state = NonGenericCrossbowAttackTask.CrossbowState.UNCHARGED;

    public NonGenericCrossbowAttackTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT), 1200);
    }

    @Override
    protected boolean shouldRun(ServerWorld serverWorld, MobEntity mobEntity) {
        LivingEntity livingEntity = NonGenericCrossbowAttackTask.getAttackTarget(mobEntity);
        return mobEntity.isHolding(Items.CROSSBOW) && LookTargetUtil.isVisibleInMemory(mobEntity, livingEntity) && LookTargetUtil.isTargetWithinAttackRange(mobEntity, livingEntity, 0);
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld serverWorld, MobEntity mobEntity, long l) {
        return mobEntity.getBrain().hasMemoryModule(MemoryModuleType.ATTACK_TARGET) && this.shouldRun(serverWorld, mobEntity);
    }

    @Override
    protected void keepRunning(ServerWorld serverWorld, MobEntity mobEntity, long l) {
        LivingEntity livingEntity = NonGenericCrossbowAttackTask.getAttackTarget(mobEntity);
        this.setLookTarget(mobEntity, livingEntity);
        this.tickState(mobEntity, livingEntity);
    }

    @Override
    protected void finishRunning(ServerWorld serverWorld, MobEntity mobEntity, long l) {
        if (mobEntity.isUsingItem()) {
            mobEntity.clearActiveItem();
        }
        if (mobEntity.isHolding(Items.CROSSBOW)) {
            ((CrossbowUser)mobEntity).setCharging(false);
            CrossbowItem.setCharged(mobEntity.getActiveItem(), false);
        }
    }

    private void tickState(MobEntity entity, LivingEntity target) {
        if (this.state == NonGenericCrossbowAttackTask.CrossbowState.UNCHARGED) {
            entity.setCurrentHand(ProjectileUtil.getHandPossiblyHolding(entity, Items.CROSSBOW));
            this.state = NonGenericCrossbowAttackTask.CrossbowState.CHARGING;
            ((CrossbowUser)entity).setCharging(true);
        } else if (this.state == NonGenericCrossbowAttackTask.CrossbowState.CHARGING) {
            ItemStack itemStack;
            int i;
            if (!entity.isUsingItem()) {
                this.state = NonGenericCrossbowAttackTask.CrossbowState.UNCHARGED;
            }
            if ((i = entity.getItemUseTime()) >= CrossbowItem.getPullTime(itemStack = entity.getActiveItem())) {
                entity.stopUsingItem();
                this.state = NonGenericCrossbowAttackTask.CrossbowState.CHARGED;
                this.chargingCooldown = 20 + entity.getRandom().nextInt(20);
                ((CrossbowUser)entity).setCharging(false);
            }
        } else if (this.state == NonGenericCrossbowAttackTask.CrossbowState.CHARGED) {
            --this.chargingCooldown;
            if (this.chargingCooldown == 0) {
                this.state = NonGenericCrossbowAttackTask.CrossbowState.READY_TO_ATTACK;
            }
        } else if (this.state == NonGenericCrossbowAttackTask.CrossbowState.READY_TO_ATTACK) {
            ((RangedAttackMob)entity).attack(target, 1.0f);
            ItemStack itemStack2 = entity.getStackInHand(ProjectileUtil.getHandPossiblyHolding(entity, Items.CROSSBOW));
            CrossbowItem.setCharged(itemStack2, false);
            this.state = NonGenericCrossbowAttackTask.CrossbowState.UNCHARGED;
        }
    }

    private void setLookTarget(MobEntity entity, LivingEntity target) {
        entity.getBrain().remember(MemoryModuleType.LOOK_TARGET, new EntityLookTarget(target, true));
    }

    private static LivingEntity getAttackTarget(LivingEntity entity) {
        return entity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
    }

    enum CrossbowState {
        UNCHARGED,
        CHARGING,
        CHARGED,
        READY_TO_ATTACK;

    }

}
