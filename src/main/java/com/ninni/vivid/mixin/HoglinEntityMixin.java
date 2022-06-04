package com.ninni.vivid.mixin;

import com.ninni.vivid.api.ICaptive;
import com.ninni.vivid.entity.VividEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoglinEntity.class)
public class HoglinEntityMixin implements ICaptive {
    private static final TrackedData<Boolean> IS_CAPTIVE = DataTracker.registerData(HoglinEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Inject(at = @At("HEAD"), method = "initialize")
    private void initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        HoglinEntity $this = (HoglinEntity) (Object) this;
        //5% chance of spawning a captive hoglin
        if (world.getRandom().nextInt(5) == 0) {
            MobEntity mobEntity = VividEntities.PIGLIN_VENATOR.create(world.toServerWorld());
            this.initializeRider(world, difficulty, mobEntity, new ZombieEntity.ZombieData(ZombieEntity.shouldBeBaby($this.getRandom()), false));
            this.setCaptive(true);
            $this.setBaby(false);
        }
    }

    @Inject(at = @At("HEAD"), method = "canBeHunted", cancellable = true)
    private void canBeHunted(CallbackInfoReturnable<Boolean> cir) {
        //Piglins will not hunt captive hoglins
        if (this.isCaptive()) {
            cir.setReturnValue(false);
        }
    }

    private EntityData initializeRider(ServerWorldAccess world, LocalDifficulty difficulty, MobEntity rider, @Nullable EntityData entityData) {
        //Venator will ride on hoglin
        HoglinEntity $this = (HoglinEntity) (Object) this;
        rider.refreshPositionAndAngles($this.getX(), $this.getY(), $this.getZ(), $this.getYaw(), 0.0f);
        rider.initialize(world, difficulty, SpawnReason.JOCKEY, entityData, null);
        rider.startRiding($this, true);
        return new PassiveEntity.PassiveData(0.0f);
    }

    @Inject(at = @At("HEAD"), method = "initDataTracker")
    private void initDataTracker(CallbackInfo ci) {
        ((HoglinEntity)(Object)this).getDataTracker().startTracking(IS_CAPTIVE, false);
    }

    @Inject(at = @At("RETURN"), method = "writeCustomDataToNbt")
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("Captive", this.isCaptive());
    }

    @Inject(at = @At("RETURN"), method = "readCustomDataFromNbt")
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        this.setCaptive(nbt.getBoolean("Captive"));
    }

    @Override
    public void setCaptive(boolean captive) {
        ((HoglinEntity)(Object)this).getDataTracker().set(IS_CAPTIVE, captive);
    }

    @Override
    public boolean isCaptive() {
        return ((HoglinEntity)(Object)this).getDataTracker().get(IS_CAPTIVE);
    }
}
