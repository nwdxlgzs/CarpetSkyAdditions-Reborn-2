package com.jsorrell.carpetskyadditions.mixin;


import com.jsorrell.carpetskyadditions.helpers.WanderingTraderHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.*;

@Mixin(WanderingTrader.class)
public abstract class WanderingTraderMixin extends AbstractVillager {
    @Shadow
    protected abstract void registerGoals();

    public WanderingTraderMixin(EntityType<? extends AbstractVillager> entityType, Level level) {
        super(entityType, level);
    }


    @Inject(method = "updateTrades", at = @At("RETURN"))
    private void onUpdateTrades(CallbackInfo ci) {
        WanderingTrader trader = (WanderingTrader)(Object)this;
        MerchantOffers offers = trader.getOffers();
        RandomSource random = trader.getRandom();

        // 1. 清除所有旧的自定义交易（通过标记）
        Iterator<MerchantOffer> iterator = offers.iterator();
        while (iterator.hasNext()) {
            MerchantOffer offer = iterator.next();
            ItemStack result = offer.getResult();
            CustomData customData = result.get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                CompoundTag tag = customData.copyTag();
                if (tag.getBooleanOr(WanderingTraderHelper.CUSTOM_TRADE_TAG, false)) {
                    iterator.remove();
                }
            }
        }

        // 2. 获取当前有效自定义交易列表
        List<MerchantOffer> customTrades = WanderingTraderHelper.getCustomOffers();
        int poolSize = customTrades.size();
        if (poolSize == 0) return;

        // 3. 根据池大小动态决定替换数量（线性递减概率）
        int maxReplace = Math.min(poolSize, offers.size());
        int replaceCount = selectReplaceCount(random, maxReplace);

        if (replaceCount == 0) return;

        // 4. 从自定义池中随机选取 replaceCount 个不同的交易
        List<MerchantOffer> selected = new ArrayList<>();
        List<MerchantOffer> tempPool = new ArrayList<>(customTrades);
        Collections.shuffle(tempPool, new Random(random.nextLong()));
        for (int i = 0; i < replaceCount; i++) {
            selected.add(tempPool.get(i).copy());
        }

        // 5. 从原 offers 中随机选择 replaceCount 个不同位置
        int originalSize = offers.size();
        Set<Integer> indices = new HashSet<>();
        while (indices.size() < replaceCount) {
            indices.add(random.nextInt(originalSize));
        }

        // 6. 替换
        List<Integer> indexList = new ArrayList<>(indices);
        for (int i = 0; i < replaceCount; i++) {
            offers.set(indexList.get(i), selected.get(i));
        }
    }

    /**
     * 根据池大小 max 返回替换数量，数量越大概率越低（线性递减）。
     * 例如 max=3 时：0的概率0.5, 1的概率0.3, 2的概率0.15, 3的概率0.05（线性权重）。
     */
    private static int selectReplaceCount(RandomSource random, int max) {
        if (max <= 0) return 0;
        // 计算权重和（权重 = max - k + 1）
        int totalWeight = 0;
        for (int k = 0; k <= max; k++) {
            totalWeight += (max - k + 1);
        }
        int r = random.nextInt(totalWeight);
        int cumulative = 0;
        for (int k = 0; k <= max; k++) {
            cumulative += (max - k + 1);
            if (r < cumulative) {
                return k;
            }
        }
        return 0;
    }

}
