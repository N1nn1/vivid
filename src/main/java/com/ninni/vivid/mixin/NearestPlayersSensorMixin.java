package com.ninni.vivid.mixin;

import com.ninni.vivid.api.ICaptive;
import com.ninni.vivid.effects.VividStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.NearestPlayersSensor;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mixin(NearestPlayersSensor.class)
public class NearestPlayersSensorMixin {

    @Inject(at = @At("HEAD"), method = "sense", cancellable = true)
    private void sense(ServerWorld world, LivingEntity entity, CallbackInfo ci) {
        //Captive hoglins will not target the player if they have the blessing
        if (entity instanceof HoglinEntity hoglinEntity && ((ICaptive)hoglinEntity).isCaptive()) {
            List<PlayerEntity> list = world.getPlayers().stream().filter(EntityPredicates.EXCEPT_SPECTATOR).filter(player -> entity.isInRange((Entity)player, 16.0)).sorted(Comparator.comparingDouble(entity::squaredDistanceTo)).collect(Collectors.toList());
            Brain<?> brain = entity.getBrain();
            brain.remember(MemoryModuleType.NEAREST_PLAYERS, list);
            List<PlayerEntity> list2 = list.stream().filter(player -> NearestPlayersSensor.testTargetPredicate(entity, player)).collect(Collectors.toList());
            brain.remember(MemoryModuleType.NEAREST_VISIBLE_PLAYER, list2.isEmpty() ? null : list2.get(0));
            Optional<PlayerEntity> optional = list2.stream().filter(player -> NearestPlayersSensor.testAttackableTargetPredicate(entity, player)).findFirst();
            if (optional.isPresent() && optional.get().hasStatusEffect(VividStatusEffects.HOGLIN_BLESSING)) {
                brain.remember(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, Optional.empty());
                ci.cancel();
            }
        }
    }

}
