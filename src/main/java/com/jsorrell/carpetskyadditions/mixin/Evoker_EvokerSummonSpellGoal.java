package com.jsorrell.carpetskyadditions.mixin;


import net.minecraft.world.entity.monster.illager.Evoker.EvokerSummonSpellGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings.noAttackEvoker;

@Mixin(EvokerSummonSpellGoal.class)
public class Evoker_EvokerSummonSpellGoal {

    @Inject(method = "performSpellCasting", at = @At("HEAD"), cancellable = true)
    private void onPerformSpellCasting(CallbackInfo ci) {
        if (noAttackEvoker) {
            ci.cancel();  // 阻止恼鬼生成
        }
    }

}
