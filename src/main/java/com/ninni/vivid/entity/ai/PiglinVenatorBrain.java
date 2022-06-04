package com.ninni.vivid.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.ninni.vivid.entity.PiglinVenatorEntity;
import com.ninni.vivid.mixin.PiglinBrainAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.task.CrossbowAttackTask;
import net.minecraft.entity.ai.brain.task.FindEntityTask;
import net.minecraft.entity.ai.brain.task.FindInteractionTargetTask;
import net.minecraft.entity.ai.brain.task.FollowMobTask;
import net.minecraft.entity.ai.brain.task.ForgetAttackTargetTask;
import net.minecraft.entity.ai.brain.task.GoToIfNearbyTask;
import net.minecraft.entity.ai.brain.task.GoToNearbyPositionTask;
import net.minecraft.entity.ai.brain.task.LookAroundTask;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.MeleeAttackTask;
import net.minecraft.entity.ai.brain.task.OpenDoorsTask;
import net.minecraft.entity.ai.brain.task.RandomTask;
import net.minecraft.entity.ai.brain.task.RangedApproachTask;
import net.minecraft.entity.ai.brain.task.StrollTask;
import net.minecraft.entity.ai.brain.task.UpdateAttackTargetTask;
import net.minecraft.entity.ai.brain.task.WaitTask;
import net.minecraft.entity.ai.brain.task.WanderAroundTask;
import net.minecraft.entity.mob.AbstractPiglinEntity;

import java.util.Optional;

public class PiglinVenatorBrain {

    public static Brain<?> create(PiglinVenatorEntity piglinBrute, Brain<PiglinVenatorEntity> brain) {
        PiglinVenatorBrain.addCoreActivities(piglinBrute, brain);
        PiglinVenatorBrain.addIdleActivities(piglinBrute, brain);
        PiglinVenatorBrain.addFightActivities(piglinBrute, brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();
        return brain;
    }

    private static void addCoreActivities(PiglinVenatorEntity piglinBrute, Brain<PiglinVenatorEntity> brain) {
        brain.setTaskList(Activity.CORE, 0, ImmutableList.of(
                new LookAroundTask(45, 90),
                new WanderAroundTask(),
                new OpenDoorsTask(),
                new VenatorForgetAngryTask()
        ));
    }

    private static void addIdleActivities(PiglinVenatorEntity piglinBrute, Brain<PiglinVenatorEntity> brain) {
        brain.setTaskList(Activity.IDLE, 10, ImmutableList.of(
                new UpdateAttackTargetTask<>(PiglinVenatorBrain::getTarget),
                PiglinVenatorBrain.method_30244(),
                PiglinVenatorBrain.method_30254(),
                new FindInteractionTargetTask(EntityType.PLAYER, 4))
        );
    }

    private static void addFightActivities(PiglinVenatorEntity piglinBrute, Brain<PiglinVenatorEntity> brain) {
        brain.setTaskList(Activity.FIGHT, 10, ImmutableList.of(
                new ForgetAttackTargetTask<>(livingEntity -> !PiglinVenatorBrain.isTarget(piglinBrute, livingEntity)),
                new RangedApproachTask(1.0f),
                new MeleeAttackTask(20),
                new NonGenericCrossbowAttackTask()
                ), MemoryModuleType.ATTACK_TARGET);
    }

    private static RandomTask<PiglinVenatorEntity> method_30244() {
        return new RandomTask<>(ImmutableList.of(
                Pair.of(new FollowMobTask(EntityType.PLAYER, 8.0f), 1),
                Pair.of(new FollowMobTask(EntityType.PIGLIN, 8.0f), 1),
                Pair.of(new FollowMobTask(EntityType.PIGLIN_BRUTE, 8.0f), 1),
                Pair.of(new FollowMobTask(8.0f), 1),
                Pair.of(new WaitTask(30, 60), 1)));
    }

    private static RandomTask<PiglinVenatorEntity> method_30254() {
        return new RandomTask<>(ImmutableList.of(
                Pair.of(new StrollTask(0.6f), 2),
                Pair.of(FindEntityTask.create(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6f, 2), 2), Pair.of(FindEntityTask.create(EntityType.PIGLIN_BRUTE, 8, MemoryModuleType.INTERACTION_TARGET, 0.6f, 2), 2), Pair.of(new GoToNearbyPositionTask(MemoryModuleType.HOME, 0.6f, 2, 100), 2), Pair.of(new GoToIfNearbyTask(MemoryModuleType.HOME, 0.6f, 5), 2), Pair.of(new WaitTask(30, 60), 1)));
    }

    public static void tick(PiglinVenatorEntity piglinBrute) {
        Brain<PiglinVenatorEntity> brain = piglinBrute.getBrain();
        Activity activity = brain.getFirstPossibleNonCoreActivity().orElse(null);
        brain.resetPossibleActivities(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
        Activity activity2 = brain.getFirstPossibleNonCoreActivity().orElse(null);
        if (activity != activity2) {
            PiglinVenatorBrain.playSoundIfAngry(piglinBrute);
        }
        piglinBrute.setAttacking(brain.hasMemoryModule(MemoryModuleType.ATTACK_TARGET));
    }

    private static boolean isTarget(AbstractPiglinEntity piglin, LivingEntity entity) {
        return PiglinVenatorBrain.getTarget(piglin).filter(livingEntity2 -> livingEntity2 == entity).isPresent();
    }

    private static Optional<? extends LivingEntity> getTarget(AbstractPiglinEntity piglin) {
        Optional<LivingEntity> optional = LookTargetUtil.getEntity(piglin, MemoryModuleType.ANGRY_AT);
        if (optional.isPresent() && Sensor.testAttackableTargetPredicateIgnoreVisibility(piglin, optional.get())) {
            return optional;
        }
        Optional<? extends LivingEntity> optional2 = PiglinVenatorBrain.method_30249(piglin, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
        if (optional2.isPresent()) {
            return optional2;
        }
        return piglin.getBrain().getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
    }

    private static Optional<? extends LivingEntity> method_30249(AbstractPiglinEntity piglin, MemoryModuleType<? extends LivingEntity> memoryModuleType) {
        return piglin.getBrain().getOptionalMemory(memoryModuleType).filter(livingEntity -> livingEntity.isInRange(piglin, 12.0));
    }

    protected static void tryRevenge(PiglinVenatorEntity piglinBrute, LivingEntity target) {
        if (target instanceof AbstractPiglinEntity) {
            return;
        }
        PiglinBrainAccessor.callTryRevenge(piglinBrute, target);
    }

    protected static void setTarget(PiglinVenatorEntity piglinBrute, LivingEntity target) {
        piglinBrute.getBrain().forget(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        piglinBrute.getBrain().remember(MemoryModuleType.ANGRY_AT, target.getUuid(), 600L);
    }

    public static void playSoundRandomly(PiglinVenatorEntity piglinBrute) {
        if ((double)piglinBrute.world.random.nextFloat() < 0.0125) {
            PiglinVenatorBrain.playSoundIfAngry(piglinBrute);
        }
    }

    private static void playSoundIfAngry(PiglinVenatorEntity piglinBrute) {
        piglinBrute.getBrain().getFirstPossibleNonCoreActivity().ifPresent(activity -> {
            if (activity == Activity.FIGHT) {
                piglinBrute.playAngrySound();
            }
        });
    }


}