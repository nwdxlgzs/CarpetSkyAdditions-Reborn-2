package com.jsorrell.carpetskyadditions.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(Cat.class)
public abstract class CatMixin {

    @Unique
    private static final Random RANDOM = new Random();

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void onMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = ((Cat) (Object) this).level();
        // 仅在服务端执行逻辑
        if (level.isClientSide()) {
            return;
        }

        ItemStack stack = player.getItemInHand(hand);
        // 条件：主手、物品是曲奇、数量 ≥ 64
        if (hand != InteractionHand.MAIN_HAND || !stack.is(Items.COOKIE) || stack.getCount() < 64) {
            return;
        }

        // --- 消耗整组曲奇 ---
        stack.shrink(64);

        // --- 10% 概率判定 ---
        if (RANDOM.nextInt(100) < 10) { // 0-99，小于10即10%
            ItemStack randomItem = getRandomItemStack();
            if (!randomItem.isEmpty()) {
                dropItemAtEntity(randomItem, (Cat) (Object) this);
            }
        }

        // 交互成功，取消原版的所有后续处理（避免原版再吃食物、驯服等）
        cir.setReturnValue(InteractionResult.SUCCESS);
    }

    /**
     * 从游戏已注册的所有物品中随机选一个（包括模组物品）
     */
    @Unique
    private ItemStack getRandomItemStack() {
        List<ItemStack> allItems = new ArrayList<>();
        BuiltInRegistries.ITEM.stream()
            .filter(item -> item != Items.AIR)
            .forEach(item -> allItems.add(new ItemStack(item)));

        if (allItems.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return allItems.get(RANDOM.nextInt(allItems.size()));
    }

    /**
     * 在猫的位置生成掉落物，并给予随机小动量
     */
    @Unique
    private void dropItemAtEntity(ItemStack stack, Cat cat) {
        if (cat.level() instanceof ServerLevel serverLevel) {
            ItemEntity itemEntity = new ItemEntity(
                serverLevel,
                cat.getX(),
                cat.getY(),
                cat.getZ(),
                stack
            );
            itemEntity.setDeltaMovement(
                (RANDOM.nextDouble() - 0.5) * 0.3,
                0.2,
                (RANDOM.nextDouble() - 0.5) * 0.3
            );
            serverLevel.addFreshEntity(itemEntity);
        }
    }
}
