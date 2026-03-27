package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.DuplicateMapKey;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.schemas.V2832;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(V2832.class)
public class SchemaV2832Mixin {
    @ModifyArg(
        method = "*",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/datafixers/DSL;taggedChoiceLazy(Ljava/lang/String;Lcom/mojang/datafixers/types/Type;Ljava/util/Map;)Lcom/mojang/datafixers/types/templates/TaggedChoice;",
            remap = false
        ),
        index = 2,
        remap = false
    )
    private static Map<String, Supplier<TypeTemplate>> addSkyblock(Map<String, Supplier<TypeTemplate>> templates) {
        // 只修改包含 "minecraft:noise" 的 Map，避免影响其他调用（如 "minecraft:flat"、"minecraft:debug" 等）
        if (templates.containsKey("minecraft:noise")) {
            return DuplicateMapKey.duplicateMapKey("minecraft:noise", "minecraft:skyblock", templates);
        }
        return templates;
    }
}
