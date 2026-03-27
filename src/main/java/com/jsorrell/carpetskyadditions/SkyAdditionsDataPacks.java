package com.jsorrell.carpetskyadditions;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import com.jsorrell.carpetskyadditions.util.SkyAdditionsResourceLocation;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.network.chat.Component;

public class SkyAdditionsDataPacks {
    public static final SkyAdditionsResourceLocation SKYBLOCK = new SkyAdditionsResourceLocation("skyblock");
    public static final SkyAdditionsResourceLocation SKYBLOCK_MMH = new SkyAdditionsResourceLocation("skyblock_mmh");
    public static final SkyAdditionsResourceLocation SKYBLOCK_ACACIA = new SkyAdditionsResourceLocation("skyblock_acacia");
    public static final SkyAdditionsResourceLocation SKYBLOCK_NO_TREE = new SkyAdditionsResourceLocation("skyblock_no_tree");
    public static final SkyAdditionsResourceLocation SKYBLOCK_SPRUCE = new SkyAdditionsResourceLocation("skyblock_spruce");
    public static final SkyAdditionsResourceLocation SKYBLOCK_SPRUCE_LARGE = new SkyAdditionsResourceLocation("skyblock_spruce_large");
    public static final SkyAdditionsResourceLocation SKYBLOCK_BIRCH = new SkyAdditionsResourceLocation("skyblock_birch");
    public static final SkyAdditionsResourceLocation SKYBLOCK_JUNGLE = new SkyAdditionsResourceLocation("skyblock_jungle");
    public static final SkyAdditionsResourceLocation SKYBLOCK_JUNGLE_LARGE = new SkyAdditionsResourceLocation("skyblock_jungle_large");
    public static final SkyAdditionsResourceLocation SKYBLOCK_DARK_OAK = new SkyAdditionsResourceLocation("skyblock_dark_oak");
    public static final SkyAdditionsResourceLocation SKYBLOCK_AZALEA = new SkyAdditionsResourceLocation("skyblock_azalea");
    public static final SkyAdditionsResourceLocation SKYBLOCK_MANGROVE = new SkyAdditionsResourceLocation("skyblock_mangrove");
    public static final SkyAdditionsResourceLocation SKYBLOCK_CHERRY = new SkyAdditionsResourceLocation("skyblock_cherry");
    public static final SkyAdditionsResourceLocation SKYBLOCK_PALE_OAK = new SkyAdditionsResourceLocation("skyblock_pale_oak");

    public static void register() {
        // Add the embedded datapacks as an option on the create world screen
        if (!ResourceManagerHelper.registerBuiltinResourcePack(
                new SkyAdditionsResourceLocation("skyblock").getResourceLocation(),
                SkyAdditionsExtension.MOD_CONTAINER,
                Component.translatable("datapack.carpetskyadditions.skyblock"),
                ResourcePackActivationType.NORMAL)) {
            SkyAdditionsSettings.LOG.warn("Could not register built-in datapack \"skyblock\".");
        }

        if (!ResourceManagerHelper.registerBuiltinResourcePack(
                new SkyAdditionsResourceLocation("skyblock_mmh").getResourceLocation(),
                SkyAdditionsExtension.MOD_CONTAINER,
                Component.translatable("datapack.carpetskyadditions.skyblock_mmh"),
                ResourcePackActivationType.NORMAL)) {
            SkyAdditionsSettings.LOG.warn("Could not register built-in datapack \"skyblock_mmh\".");
        }

        if (!ResourceManagerHelper.registerBuiltinResourcePack(
                new SkyAdditionsResourceLocation("skyblock_acacia").getResourceLocation(),
                SkyAdditionsExtension.MOD_CONTAINER,
                Component.translatable("datapack.carpetskyadditions.acacia"),
                ResourcePackActivationType.NORMAL)) {
            SkyAdditionsSettings.LOG.warn("Could not register built-in datapack \"skyblock_acacia\".");
        }

        if (!ResourceManagerHelper.registerBuiltinResourcePack(
                new SkyAdditionsResourceLocation("skyblock_no_tree").getResourceLocation(),
                SkyAdditionsExtension.MOD_CONTAINER,
                Component.translatable("datapack.carpetskyadditions.no_tree"),
                ResourcePackActivationType.NORMAL)) {
            SkyAdditionsSettings.LOG.warn("Could not register built-in datapack \"skyblock_no_tree\".");
        }

        if (!ResourceManagerHelper.registerBuiltinResourcePack(
                new SkyAdditionsResourceLocation("skyblock_spruce").getResourceLocation(),
                SkyAdditionsExtension.MOD_CONTAINER,
                Component.translatable("datapack.carpetskyadditions.spruce"),
                ResourcePackActivationType.NORMAL)) {
            SkyAdditionsSettings.LOG.warn("Could not register built-in datapack \"skyblock_spruce\".");
        }

        if (!ResourceManagerHelper.registerBuiltinResourcePack(
                new SkyAdditionsResourceLocation("skyblock_spruce_large").getResourceLocation(),
                SkyAdditionsExtension.MOD_CONTAINER,
                Component.translatable("datapack.carpetskyadditions.spruce_large"),
                ResourcePackActivationType.NORMAL)) {
            SkyAdditionsSettings.LOG.warn("Could not register built-in datapack \"skyblock_spruce_large\".");
        }

        if (!ResourceManagerHelper.registerBuiltinResourcePack(
                new SkyAdditionsResourceLocation("skyblock_birch").getResourceLocation(),
                SkyAdditionsExtension.MOD_CONTAINER,
                Component.translatable("datapack.carpetskyadditions.birch"),
                ResourcePackActivationType.NORMAL)) {
            SkyAdditionsSettings.LOG.warn("Could not register built-in datapack \"skyblock_birch\".");
        }

        if (!ResourceManagerHelper.registerBuiltinResourcePack(
                new SkyAdditionsResourceLocation("skyblock_jungle").getResourceLocation(),
                SkyAdditionsExtension.MOD_CONTAINER,
                Component.translatable("datapack.carpetskyadditions.jungle"),
                ResourcePackActivationType.NORMAL)) {
            SkyAdditionsSettings.LOG.warn("Could not register built-in datapack \"skyblock_jungle\".");
        }

        if (!ResourceManagerHelper.registerBuiltinResourcePack(
                new SkyAdditionsResourceLocation("skyblock_jungle_large").getResourceLocation(),
                SkyAdditionsExtension.MOD_CONTAINER,
                Component.translatable("datapack.carpetskyadditions.jungle_large"),
                ResourcePackActivationType.NORMAL)) {
            SkyAdditionsSettings.LOG.warn("Could not register built-in datapack \"skyblock_jungle_large\".");
        }

        if (!ResourceManagerHelper.registerBuiltinResourcePack(
                new SkyAdditionsResourceLocation("skyblock_dark_oak").getResourceLocation(),
                SkyAdditionsExtension.MOD_CONTAINER,
                Component.translatable("datapack.carpetskyadditions.dark_oak"),
                ResourcePackActivationType.NORMAL)) {
            SkyAdditionsSettings.LOG.warn("Could not register built-in datapack \"skyblock_dark_oak\".");
        }

        if (!ResourceManagerHelper.registerBuiltinResourcePack(
                new SkyAdditionsResourceLocation("skyblock_azalea").getResourceLocation(),
                SkyAdditionsExtension.MOD_CONTAINER,
                Component.translatable("datapack.carpetskyadditions.azalea"),
                ResourcePackActivationType.NORMAL)) {
            SkyAdditionsSettings.LOG.warn("Could not register built-in datapack \"skyblock_azalea\".");
        }

        if (!ResourceManagerHelper.registerBuiltinResourcePack(
                new SkyAdditionsResourceLocation("skyblock_mangrove").getResourceLocation(),
                SkyAdditionsExtension.MOD_CONTAINER,
                Component.translatable("datapack.carpetskyadditions.mangrove"),
                ResourcePackActivationType.NORMAL)) {
            SkyAdditionsSettings.LOG.warn("Could not register built-in datapack \"skyblock_mangrove\".");
        }

        if (!ResourceManagerHelper.registerBuiltinResourcePack(
                new SkyAdditionsResourceLocation("skyblock_cherry").getResourceLocation(),
                SkyAdditionsExtension.MOD_CONTAINER,
                Component.translatable("datapack.carpetskyadditions.cherry"),
                ResourcePackActivationType.NORMAL)) {
            SkyAdditionsSettings.LOG.warn("Could not register built-in datapack \"skyblock_cherry\".");
        }

        if (!ResourceManagerHelper.registerBuiltinResourcePack(
                new SkyAdditionsResourceLocation("skyblock_pale_oak").getResourceLocation(),
                SkyAdditionsExtension.MOD_CONTAINER,
                Component.translatable("datapack.carpetskyadditions.pale_oak"),
                ResourcePackActivationType.NORMAL)) {
            SkyAdditionsSettings.LOG.warn("Could not register built-in datapack \"skyblock_pale_oak\".");
        }


    }
}
