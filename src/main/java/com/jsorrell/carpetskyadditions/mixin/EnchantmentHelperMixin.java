package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.SkyAdditionsExtension;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Stream;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    @Inject(
        method = "getAvailableEnchantmentResults",
        at = @At("RETURN")
    )
    private static void forceAllowSwiftSneak(
        int value, ItemStack stack, Stream<Holder<Enchantment>> source,
        CallbackInfoReturnable<List<EnchantmentInstance>> cir) {

        // 直接获取即将返回的列表
        List<EnchantmentInstance> results = cir.getReturnValue();

        if (SkyAdditionsExtension.minecraftServer != null) {
            Holder.Reference<Enchantment> swiftSneak = SkyAdditionsExtension.minecraftServer
                .registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .get(Enchantments.SWIFT_SNEAK)
                .orElseThrow();

            CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            boolean shouldEnchant = customData.copyTag()
                .getBoolean("SWIFT_SNEAK_ENCHANTABLE")
                .orElse(false);

            if (shouldEnchant && (swiftSneak.value().canEnchant(stack) || stack.is(Items.BOOK))) {
                for (int level = 1; level <= 3; level++) {
                    results.add(new EnchantmentInstance(swiftSneak, level));
                }
            }
        }
    }
}
