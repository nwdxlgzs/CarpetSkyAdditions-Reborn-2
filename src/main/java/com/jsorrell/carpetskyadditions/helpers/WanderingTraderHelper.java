package com.jsorrell.carpetskyadditions.helpers;

import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;
import  net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

public class WanderingTraderHelper {


    public static final String CUSTOM_TRADE_TAG = "WanderingTrader-custom_trade";
    @NotNull
    public static List<MerchantOffer> getCustomOffers() {
        List<MerchantOffer> offers = new ArrayList<>();
        if (SkyAdditionsSettings.tallFlowersFromWanderingTrader) {
            // 1绿宝石 → 1向日葵
            offers.add(createSimpleTrade(Items.EMERALD, 1, Items.SUNFLOWER, 1, 12, 1, 0.05f));
            offers.add(createSimpleTrade(Items.EMERALD, 1, Items.LILAC, 1, 12, 1, 0.05f));
            offers.add(createSimpleTrade(Items.EMERALD, 1, Items.ROSE_BUSH, 1, 12, 1, 0.05f));
            offers.add(createSimpleTrade(Items.EMERALD, 1, Items.PEONY, 1, 12, 1, 0.05f));
        }
        if (SkyAdditionsSettings.lavaFromWanderingTrader) {
            // 16绿宝石 + 1桶 → 1岩浆桶
            offers.add(createTwoInputTrade(
                Items.EMERALD, 16,
                Items.BUCKET, 1,
                Items.LAVA_BUCKET, 1,
                1, 1, 0.05f
            ));
        }
        return offers;
    }

    private static MerchantOffer createSimpleTrade(
        ItemLike wantItem, int wantCount,
        ItemLike giveItem, int giveCount,
        int maxUses, int xp, float priceMultiplier) {
        ItemCost cost = new ItemCost(wantItem, wantCount);
        ItemStack result = new ItemStack(giveItem, giveCount);
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(CUSTOM_TRADE_TAG, true);
        result.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return new MerchantOffer(cost, result, maxUses, xp, priceMultiplier);
    }

    private static MerchantOffer createTwoInputTrade(
        ItemLike wantItem1, int count1,
        ItemLike wantItem2, int count2,
        ItemLike giveItem, int giveCount,
        int maxUses, int xp, float priceMultiplier) {
        ItemCost costA = new ItemCost(wantItem1, count1);
        ItemCost costB = new ItemCost(wantItem2, count2);
        ItemStack result = new ItemStack(giveItem, giveCount);
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(CUSTOM_TRADE_TAG, true);
        result.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return new MerchantOffer(costA, Optional.of(costB), result, maxUses, xp, priceMultiplier);
    }
}
