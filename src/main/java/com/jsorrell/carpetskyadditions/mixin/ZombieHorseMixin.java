package com.jsorrell.carpetskyadditions.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.equine.ZombieHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ZombieHorse.class)
public abstract class ZombieHorseMixin extends AbstractHorse {

    @Unique
    private static final EntityDataAccessor<Integer> CONVERSION_TIME =
        SynchedEntityData.defineId(ZombieHorse.class, EntityDataSerializers.INT);

    protected ZombieHorseMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CONVERSION_TIME, -1);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        // 原版僵尸马交互逻辑：设置持久性
        this.setPersistenceRequired();

        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.GOLDEN_CARROT) && this.hasEffect(MobEffects.WEAKNESS) && !this.isConverting()) {
            if (!this.level().isClientSide()) {
                stack.consume(1, player);
                // 转换时间 40 ticks (2秒)，用于测试；正式版可改为 4000 (200秒)
                this.startConverting(40);
            }
            return InteractionResult.SUCCESS;
        }
        return super.interact(player, hand, location);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide() && this.isConverting()) {
            int time = this.entityData.get(CONVERSION_TIME);
            time--;
            if (time <= 0) {
                this.finishConversion((ServerLevel) this.level());
                this.entityData.set(CONVERSION_TIME, -1);
            } else {
                this.entityData.set(CONVERSION_TIME, time);
            }
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 16 && !this.isSilent()) {
            this.level().playLocalSound(this.getX(), this.getEyeY(), this.getZ(),
                SoundEvents.ZOMBIE_VILLAGER_CURE, this.getSoundSource(),
                1.0F + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.3F, false);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Unique
    private boolean isConverting() {
        return this.entityData.get(CONVERSION_TIME) > 0;
    }

    @Unique
    private void startConverting(int duration) {
        this.entityData.set(CONVERSION_TIME, duration);
        this.removeEffect(MobEffects.WEAKNESS);
        this.addEffect(new MobEffectInstance(MobEffects.STRENGTH, duration, 0));
        this.level().broadcastEntityEvent(this, (byte) 16);
    }

    @Unique
    private void finishConversion(ServerLevel level) {
        Horse horse = EntityType.HORSE.create(level, EntitySpawnReason.CONVERSION);
        if (horse != null) {
            horse.setPos(this.getX(), this.getY(), this.getZ());
            horse.setYRot(this.getYRot());
            horse.setXRot(this.getXRot());
            horse.setHealth(this.getHealth());
            if (this.hasCustomName()) {
                horse.setCustomName(this.getCustomName());
                horse.setCustomNameVisible(this.isCustomNameVisible());
            }
            if (this.isBaby()) {
                horse.setBaby(true);
            }
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                horse.setItemSlot(slot, this.getItemBySlot(slot));
            }
            horse.setTamed(this.isTamed());
            // 但为了更真实，可让 horse.finalizeSpawn 随机生成
            horse.finalizeSpawn(level, level.getCurrentDifficultyAt(horse.blockPosition()), EntitySpawnReason.CONVERSION, null);
            level.addFreshEntity(horse);
        }
        level.levelEvent(null, 1027, this.blockPosition(), 0);
        this.discard();
    }
}
