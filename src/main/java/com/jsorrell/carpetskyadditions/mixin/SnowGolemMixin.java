package com.jsorrell.carpetskyadditions.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.golem.AbstractGolem;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

import static com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings.existElsaSnowGolem;
import static com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings.existOlafSnowGolem;

@Mixin(SnowGolem.class)
public class SnowGolemMixin extends AbstractGolem {
    // 致敬冰雪奇缘，雪傀儡有两个特殊开关，艾莎和雪宝均可以无视融化环境，并且艾莎可以冻结脚底的水。
    private static final List<String> ELSA_NAMES = Arrays.asList("Elsa", "艾莎");
    private static final List<String> OLAF_NAMES = Arrays.asList("Olaf", "雪宝");

    protected SnowGolemMixin(EntityType<? extends AbstractGolem> type, Level level) {
        super(type, level);
    }

    @Inject(
        method = "aiStep",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/golem/SnowGolem;level()Lnet/minecraft/world/level/Level;",
            shift = At.Shift.AFTER
        )
    )
    public void onAiStep(CallbackInfo ci) {
        if (!existElsaSnowGolem) return;
        SnowGolem self = (SnowGolem) (Object) this;
        Level level = self.level();

        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        if (!(Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)) {
            return; // 如果禁用，则不生成冰块
        }
        Component customName = self.getCustomName();
        if (customName == null) {
            return;
        }

        String name = customName.getString();

        // 仅艾莎冻结水
        if (!ELSA_NAMES.contains(name)) {
            return;
        }

        BlockState iceBlock = Blocks.ICE.defaultBlockState();
        BlockState packedIceBlock = Blocks.PACKED_ICE.defaultBlockState();
        BlockState blueIceBlock = Blocks.BLUE_ICE.defaultBlockState();

        for (int i = 0; i < 4; i++) {
            int x = Mth.floor(self.getX() + (double) ((float) (i % 2 * 2 - 1) * 0.25F));
            int y = Mth.floor(self.getY() - 1);
            int z = Mth.floor(self.getZ() + (double) ((float) (i / 2 % 2 * 2 - 1) * 0.25F));

            BlockPos blockPos = new BlockPos(x, y, z);
            BlockState blockState = serverLevel.getBlockState(blockPos);

            if (!blockState.is(Blocks.WATER)) {
                continue;
            }

            FluidState fluidState = blockState.getFluidState();
            BlockState newBlock = null;

            if (fluidState.isSource() && blueIceBlock.canSurvive(serverLevel, blockPos)) {
                newBlock = blueIceBlock;
            } else if (fluidState.getAmount() >= 7 && packedIceBlock.canSurvive(serverLevel, blockPos)) {
                newBlock = packedIceBlock;
            } else if (iceBlock.canSurvive(serverLevel, blockPos)) {
                newBlock = iceBlock;
            }

            if (newBlock != null) {
                serverLevel.setBlockAndUpdate(blockPos, newBlock);
                serverLevel.gameEvent(GameEvent.BLOCK_PLACE, blockPos, GameEvent.Context.of(self, newBlock));
            }
        }
    }


    @Redirect(
        method = "aiStep",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/golem/SnowGolem;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"
        )
    )
    private boolean redirectHurtServer(SnowGolem self, ServerLevel level, DamageSource source, float amount) {
        if (existElsaSnowGolem || existOlafSnowGolem) {
            Component customName = self.getCustomName();
            if (customName != null) {
                String name = customName.getString();
                if (existElsaSnowGolem && ELSA_NAMES.contains(name)) {
                    return false; // 假装没受伤，跳过伤害调用
                }
                if (existOlafSnowGolem && OLAF_NAMES.contains(name)) {
                    return false; // 假装没受伤，跳过伤害调用
                }
            }
        }
        // 否则正常调用原方法
        return self.hurtServer(level, source, amount);
    }
}
