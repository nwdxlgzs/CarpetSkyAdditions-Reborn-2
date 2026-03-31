package com.jsorrell.carpetskyadditions.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.EnderMan;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings.banEnderManTeleport;

@Mixin(EnderMan.class)
public abstract class EnderManMixin {
    @Inject(method = "teleport(DDD)Z", at = @At("HEAD"), cancellable = true)
    private void onTeleport(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        EnderMan self = (EnderMan) (Object) this;
        if (self.level() instanceof ServerLevel serverLevel) {
            if (banEnderManTeleport) {
                cir.setReturnValue(false);
            }
        }
    }
}
