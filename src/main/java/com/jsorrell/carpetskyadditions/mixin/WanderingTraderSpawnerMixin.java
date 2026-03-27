package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTraderSpawner;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.saveddata.WanderingTraderData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WanderingTraderSpawner.class)
public abstract class WanderingTraderSpawnerMixin {
    @Unique
    private int currentSpawnTimer;

    @Shadow
    private int tickDelay;

    @Shadow
    @Final
    private RandomSource random;

    @Shadow
    protected abstract boolean spawn(ServerLevel level);

    @Shadow
    protected abstract WanderingTraderData getTraderData();

    @Unique
    private boolean usesDefaultSettings() {
        return SkyAdditionsSettings.wanderingTraderSpawnRate == 24000
            && SkyAdditionsSettings.maxWanderingTraderSpawnChance == 0.075;
    }

    @WrapOperation(method = "spawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"))
    private int skipSecondChanceCheck(RandomSource instance, int i, Operation<Integer> original) {
        WanderingTraderData data = this.getTraderData();
        int chanceToSpawn = data.spawnChance();
        return 100 < chanceToSpawn ? 0 : instance.nextInt(i);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(
        ServerLevel level, boolean spawnEnemies, CallbackInfo ci) {

        if (usesDefaultSettings()) {
            return;
        }

        ci.cancel();

        if (!level.getGameRules().get(GameRules.SPAWN_WANDERING_TRADERS)) {
            return;
        }
        WanderingTraderData data = this.getTraderData();
        int spawnDelay = data.spawnDelay();
        if (SkyAdditionsSettings.wanderingTraderSpawnRate < spawnDelay) {
            spawnDelay = SkyAdditionsSettings.wanderingTraderSpawnRate;
            data.setSpawnDelay(spawnDelay);
            currentSpawnTimer = Math.min(1200, spawnDelay);
            tickDelay = currentSpawnTimer;
        }

        if (--tickDelay > 0) {
            return;
        }

        spawnDelay -= currentSpawnTimer;
        boolean trySpawn = spawnDelay <= 0;

        spawnDelay = trySpawn ? SkyAdditionsSettings.wanderingTraderSpawnRate : spawnDelay;

        currentSpawnTimer = Math.min(1200, spawnDelay);
        tickDelay = currentSpawnTimer;

        data = this.getTraderData();
        data.setSpawnDelay(spawnDelay);
        int chanceToSpawn = data.spawnChance();

        if (trySpawn && level.getGameRules().get(GameRules.SPAWN_MOBS)) {
            if (random.nextInt(100 < chanceToSpawn ? 1000 : 100) < chanceToSpawn && spawn(level)) {
                chanceToSpawn = 25;
            } else {
                chanceToSpawn = Mth.clamp(chanceToSpawn + 25, 25,
                    (int) Math.round(SkyAdditionsSettings.maxWanderingTraderSpawnChance * 1000d));
            }
            data.setSpawnChance(chanceToSpawn);
        }
    }
}
