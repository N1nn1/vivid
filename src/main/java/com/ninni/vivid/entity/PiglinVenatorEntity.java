package com.ninni.vivid.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import com.ninni.vivid.entity.ai.PiglinVenatorBrain;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.PiglinActivity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class PiglinVenatorEntity extends AbstractPiglinEntity implements CrossbowUser {
    protected static final ImmutableList<SensorType<? extends Sensor<? super PiglinVenatorEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SensorType.PIGLIN_BRUTE_SPECIFIC_SENSOR);
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULE_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.MOBS, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.HOME);
    private static final TrackedData<Boolean> CHARGING = DataTracker.registerData(PiglinVenatorEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public PiglinVenatorEntity(EntityType<? extends PiglinVenatorEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 20;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(CHARGING, false);
    }

    @Override
    protected void mobTick() {
        this.world.getProfiler().push("piglinVenatorBrain");
        this.getBrain().tick((ServerWorld)this.world, this);
        this.world.getProfiler().pop();
        PiglinVenatorBrain.tick(this);
        PiglinVenatorBrain.playSoundRandomly(this);
        super.mobTick();
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

    @Override
    protected Brain.Profile<PiglinVenatorEntity> createBrainProfile() {
        return Brain.createProfile(MEMORY_MODULE_TYPES, SENSOR_TYPES);
    }

    @Override
    public Brain<PiglinVenatorEntity> getBrain() {
        return (Brain<PiglinVenatorEntity>) super.getBrain();
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return PiglinVenatorBrain.create(this, this.createBrainProfile().deserialize(dynamic));
    }

    @Override
    protected boolean canHunt() {
        return true;
    }

    @Override
    public PiglinActivity getActivity() {
        if (this.isAttacking() && this.isHoldingTool()) {
            return PiglinActivity.ATTACKING_WITH_MELEE_WEAPON;
        }
        if (this.isCharging()) {
            return PiglinActivity.CROSSBOW_CHARGE;
        }
        if (this.isAttacking() && this.isHolding(Items.CROSSBOW)) {
            return PiglinActivity.CROSSBOW_HOLD;
        }
        return PiglinActivity.DEFAULT;
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.equipStack(EquipmentSlot.MAINHAND, this.makeInitialWeapon());
        return entityData;
    }

    @Override
    protected void playZombificationSound() {
        this.playSound(SoundEvents.ENTITY_PIGLIN_CONVERTED_TO_ZOMBIFIED);
    }

    protected void playSound(SoundEvent sound) {
        this.playSound(sound, this.getSoundVolume(), this.getSoundPitch());
    }

    private boolean isCharging() {
        return this.dataTracker.get(CHARGING);
    }

    @Override
    public void setCharging(boolean charging) {
        this.dataTracker.set(CHARGING, charging);
    }

    @Override
    public void shoot(LivingEntity target, ItemStack crossbow, ProjectileEntity projectile, float multiShotSpray) {
        this.shoot(this, target, projectile, multiShotSpray, 1.6f);
    }

    @Override
    public void postShoot() {
        this.despawnCounter = 0;
    }

    public void playAngrySound() {
        this.playSound(SoundEvents.ENTITY_PIGLIN_BRUTE_ANGRY, 1.0f, this.getSoundPitch());
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        this.shoot(this, 1.6F);
    }

    @Override
    public boolean canUseRangedWeapon(RangedWeaponItem weapon) {
        return weapon == Items.CROSSBOW;
    }

    private ItemStack makeInitialWeapon() {
        if ((double)this.random.nextFloat() < 0.5) {
            return new ItemStack(Items.CROSSBOW);
        }
        return new ItemStack(Items.GOLDEN_SWORD);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isClient()) {
            System.out.println(this.getMainHandStack());
        }
    }
}
