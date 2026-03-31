package com.jsorrell.carpetskyadditions.mixin;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner.SpawnState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings.pseudoPeace;

@Mixin(SpawnState.class)
public class NaturalSpawner_SpawnStateMixin {
    /**
     * 拦截 canSpawnForCategoryGlobal 方法，对怪物类别返回 false，
     * 使游戏认为该类别怪物已达到上限，从而禁止自然生成。
     */
    @Inject(method = "canSpawnForCategoryGlobal", at = @At("RETURN"), cancellable = true)
    private void onCanSpawnForCategoryGlobal(MobCategory category, CallbackInfoReturnable<Boolean> cir) {
        // 如果是怪物类别（MONSTER），强制返回 false
        if (category == MobCategory.MONSTER) {
            if (pseudoPeace)
                cir.setReturnValue(false);
        }
        // 其他类别（如 CREATURE、AMBIENT 等）保持原值
    }
}
