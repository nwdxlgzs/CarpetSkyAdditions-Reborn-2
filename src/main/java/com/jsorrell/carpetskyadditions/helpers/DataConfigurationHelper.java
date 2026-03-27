package com.jsorrell.carpetskyadditions.helpers;

import com.jsorrell.carpetskyadditions.SkyAdditionsDataPacks;
import com.jsorrell.carpetskyadditions.config.SkyAdditionsConfig;
import java.util.ArrayList;
import java.util.List;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.WorldDataConfiguration;

public class DataConfigurationHelper {
    public static WorldDataConfiguration updateDataConfiguration(WorldDataConfiguration dc) {
        SkyAdditionsConfig config =
            AutoConfig.getConfigHolder(SkyAdditionsConfig.class).get();
        if (config.enableDatapackByDefault) {
            List<String> enabled = new ArrayList<>(dc.dataPacks().getEnabled());
            List<String> disabled = new ArrayList<>(dc.dataPacks().getDisabled());

            String skyBlock = SkyAdditionsDataPacks.SKYBLOCK.toString();
            String acacia = SkyAdditionsDataPacks.SKYBLOCK_ACACIA.toString();
            String spruce = SkyAdditionsDataPacks.SKYBLOCK_SPRUCE.toString();
            String spruceLarge = SkyAdditionsDataPacks.SKYBLOCK_SPRUCE_LARGE.toString();
            String birch = SkyAdditionsDataPacks.SKYBLOCK_BIRCH.toString();
            String jungle = SkyAdditionsDataPacks.SKYBLOCK_JUNGLE.toString();
            String jungleLarge = SkyAdditionsDataPacks.SKYBLOCK_JUNGLE_LARGE.toString();
            String darkOak = SkyAdditionsDataPacks.SKYBLOCK_DARK_OAK.toString();
            String azalea = SkyAdditionsDataPacks.SKYBLOCK_AZALEA.toString();
            String mangrove = SkyAdditionsDataPacks.SKYBLOCK_MANGROVE.toString();
            String cherry = SkyAdditionsDataPacks.SKYBLOCK_CHERRY.toString();
            String paleOak = SkyAdditionsDataPacks.SKYBLOCK_PALE_OAK.toString();

            if (!enabled.contains(skyBlock)) {
                enabled.add(skyBlock);
                disabled.remove(skyBlock);
            }

            SkyAdditionsConfig.InitialTreeType initialTreeType = config.getInitialTreeType();
            switch (initialTreeType) {
                case SkyAdditionsConfig.InitialTreeType.ACACIA:{
                    if (!enabled.contains(acacia)) {
                        enabled.add(acacia);
                        disabled.remove(acacia);
                    }
                    break;
                }
                case SkyAdditionsConfig.InitialTreeType.SPRUCE:{
                    if (!enabled.contains(spruce)) {
                        enabled.add(spruce);
                        disabled.remove(spruce);
                    }
                    break;
                }
                case SkyAdditionsConfig.InitialTreeType.SPRUCE_LARGE:{
                    if (!enabled.contains(spruceLarge)) {
                        enabled.add(spruceLarge);
                        disabled.remove(spruceLarge);
                    }
                    break;
                }
                case SkyAdditionsConfig.InitialTreeType.BIRCH:{
                    if (!enabled.contains(birch)) {
                        enabled.add(birch);
                        disabled.remove(birch);
                    }
                    break;
                }
                case SkyAdditionsConfig.InitialTreeType.JUNGLE:{
                    if (!enabled.contains(jungle)) {
                        enabled.add(jungle);
                        disabled.remove(jungle);
                    }
                    break;
                }
                case SkyAdditionsConfig.InitialTreeType.JUNGLE_LARGE:{
                    if (!enabled.contains(jungleLarge)) {
                        enabled.add(jungleLarge);
                        disabled.remove(jungleLarge);
                    }
                    break;
                }
                case SkyAdditionsConfig.InitialTreeType.DARK_OAK:{
                    if (!enabled.contains(darkOak)) {
                        enabled.add(darkOak);
                        disabled.remove(darkOak);
                    }
                    break;
                }
                case SkyAdditionsConfig.InitialTreeType.AZALEA:{
                    if (!enabled.contains(azalea)) {
                        enabled.add(azalea);
                        disabled.remove(azalea);
                    }
                    break;
                }
                case SkyAdditionsConfig.InitialTreeType.MANGROVE:{
                    if (!enabled.contains(mangrove)) {
                        enabled.add(mangrove);
                        disabled.remove(mangrove);
                    }
                    break;
                }
                case SkyAdditionsConfig.InitialTreeType.CHERRY:{
                    if (!enabled.contains(cherry)) {
                        enabled.add(cherry);
                        disabled.remove(cherry);
                    }
                    break;
                }
                case SkyAdditionsConfig.InitialTreeType.PALE_OAK:{
                    if (!enabled.contains(paleOak)) {
                        enabled.add(paleOak);
                        disabled.remove(paleOak);
                    }
                    break;
                }
            }
            return new WorldDataConfiguration(new DataPackConfig(enabled, disabled), dc.enabledFeatures());
        }
        return dc;
    }
}
