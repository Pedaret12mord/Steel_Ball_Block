package net.chargablepipe.keeplooking4ward.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AnimatedBallItem extends Item {

    public AnimatedBallItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(
            Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            stack.getOrCreateTag().putBoolean("Charging", true);
        }

        player.startUsingItem(hand);

        return InteractionResultHolder.consume(stack);
    }


    public void OnUseTick()
    {

    }

    @Override
    public void releaseUsing(
            ItemStack stack, Level level, LivingEntity entity, int timeLeft) {

        if (!level.isClientSide()) {
            stack.getOrCreateTag().putBoolean("Charging", false);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
}
