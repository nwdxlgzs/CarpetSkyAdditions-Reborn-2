package com.jsorrell.carpetskyadditions.advancements.predicates;

import com.jsorrell.carpetskyadditions.util.SkyAdditionsResourceLocation;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SkyAdditionsLootItemConditions {
    public static final MapCodec<? extends LootItemCondition> LOCATION_CHECK =
        register("location_check", SkyAdditionsLocationCheck.CODEC);
    public static final MapCodec<? extends LootItemCondition> ENTITY_PROPERTIES =
        register("entity_properties", SkyAdditionsLootItemEntityPropertyCondition.CODEC);

    private static MapCodec<? extends LootItemCondition> register(
        String registryName, MapCodec<? extends LootItemCondition> codec) {
        return Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            new SkyAdditionsResourceLocation(registryName).getResourceLocation(),
            codec);
    }

    public static void bootstrap() {
    }
}
