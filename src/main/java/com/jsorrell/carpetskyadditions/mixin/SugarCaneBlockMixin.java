package com.jsorrell.carpetskyadditions.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SugarCaneBlock.class)
public abstract class SugarCaneBlockMixin extends Block implements BonemealableBlock {

    public SugarCaneBlockMixin(Properties properties) {
        super(properties);
    }

    // 获取植株底部位置
    private BlockPos getBottomPos(LevelReader level, BlockPos pos) {
        BlockPos bottom = pos;
        while (level.getBlockState(bottom.below()).is(Blocks.SUGAR_CANE)) {
            bottom = bottom.below();
        }
        return bottom;
    }

    // 获取当前总高度（节数）
    private int getTotalHeight(LevelReader level, BlockPos bottom) {
        int height = 1;
        while (level.getBlockState(bottom.above(height)).is(Blocks.SUGAR_CANE)) {
            height++;
        }
        return height;
    }

    // 获取当前顶端位置
    private BlockPos getTopPos(LevelReader level, BlockPos bottom) {
        int height = getTotalHeight(level, bottom);
        return bottom.above(height - 1);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        BlockPos bottom = getBottomPos(level, pos);
        BlockPos top = getTopPos(level, bottom);
        BlockPos aboveTop = top.above();

        // 上方必须是空气，且在世界建筑高度范围内
        if (!level.getBlockState(aboveTop).isAir() || !level.isInsideBuildHeight(aboveTop)) {
            return false;
        }

        // Y >= 0 时，限制最大高度为 3
        if (bottom.getY() >= 0) {
            int currentHeight = getTotalHeight(level, bottom);
            return currentHeight < 3;
        }

        // Y < 0 时，只要上方是空气且未超建筑高度即可催熟
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos bottom = getBottomPos(level, pos);
        BlockPos top = getTopPos(level, bottom);

        if (bottom.getY() >= 0) {
            // Y >= 0：一次性生长到 3 节
            int currentHeight = getTotalHeight(level, bottom);
            for (int i = currentHeight; i < 3; i++) {
                BlockPos newPos = bottom.above(i);
                if (level.getBlockState(newPos).isAir() && level.isInsideBuildHeight(newPos)) {
                    level.setBlock(newPos, Blocks.SUGAR_CANE.defaultBlockState(), Block.UPDATE_CLIENTS);
                } else {
                    break;
                }
            }
        } else {
            // Y < 0：每次骨粉只长 1 节（参考竹子）
            BlockPos aboveTop = top.above();
            if (level.getBlockState(aboveTop).isAir() && level.isInsideBuildHeight(aboveTop)) {
                level.setBlock(aboveTop, Blocks.SUGAR_CANE.defaultBlockState(), Block.UPDATE_CLIENTS);
            }
        }
    }

}
