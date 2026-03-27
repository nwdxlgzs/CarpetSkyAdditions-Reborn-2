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
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.camel.CamelHusk;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CamelHusk.class)
public abstract class CamelHuskMixin extends Camel {

    @Unique
    private static final EntityDataAccessor<Integer> CONVERSION_TIME =
        SynchedEntityData.defineId(CamelHusk.class, EntityDataSerializers.INT);

    protected CamelHuskMixin(EntityType<? extends Camel> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CONVERSION_TIME, -1);   // -1 表示未转换
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        // 原版骆驼尸壳交互逻辑：设置持久性
        this.setPersistenceRequired();

        ItemStack stack = player.getItemInHand(hand);
        // 条件：手持仙人掌，自身有虚弱效果，且不在转换中
        if (stack.is(Items.CACTUS) && this.hasEffect(MobEffects.WEAKNESS) && !this.isConverting()) {
            if (!this.level().isClientSide()) {
                stack.consume(1, player);
                this.startConverting(4000);
            }
            return InteractionResult.SUCCESS;
        }
        // 否则调用父类方法（原版交互逻辑）
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
            // 播放治愈音效（复用僵尸村民治愈音效）
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
        Camel camel = EntityType.CAMEL.create(level, EntitySpawnReason.CONVERSION);
        if (camel != null) {
            camel.setPos(this.getX(), this.getY(), this.getZ());
            camel.setYRot(this.getYRot());
            camel.setXRot(this.getXRot());
            camel.setHealth(this.getHealth());
            if (this.hasCustomName()) {
                camel.setCustomName(this.getCustomName());
                camel.setCustomNameVisible(this.isCustomNameVisible());
            }
            if (this.isBaby()) {
                camel.setBaby(true);
            }
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                camel.setItemSlot(slot, this.getItemBySlot(slot));
            }
            level.addFreshEntity(camel);
        }
        level.levelEvent(null, 1027, this.blockPosition(), 0);
        this.discard();
    }
}
