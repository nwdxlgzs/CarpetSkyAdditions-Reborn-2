package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.warden.SonicBoom;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.dolphin.Dolphin;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SonicBoom.class)
public class SonicBoomMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void dropEchoShard(ServerLevel level, Warden warden, long timestamp, CallbackInfo ci) {
        if (SkyAdditionsSettings.renewableEchoShards) {
            warden.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent(target -> {
                if (target instanceof Dolphin || target instanceof Bat) {
                    if (target.isDeadOrDying()) {
                        target.spawnAtLocation(level, Items.ECHO_SHARD);
                    }
                }
            });
        }
    }
}
