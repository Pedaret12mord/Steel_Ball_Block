package net.chargablepipe.keeplooking4ward.item.custom;

import net.chargablepipe.keeplooking4ward.client.ClientChargeSound;
import net.chargablepipe.keeplooking4ward.entity.custom.WreckingBallProjectile;
import net.chargablepipe.keeplooking4ward.sound.ModSounds;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public class WreckingItem extends Item {
    public WreckingItem(Properties p_41383_) {
        super(p_41383_);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(
            Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        p_41433_.startUsingItem(p_41434_);
        return InteractionResultHolder.consume(p_41433_.getItemInHand(p_41434_));
        //return super.use(p_41432_, p_41433_, p_41434_);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public void onUseTick(
            Level level,
            LivingEntity entity,
            ItemStack stack,
            int remainingUseDuration
    ) {
        if (level.isClientSide) {
            int ticksUsed = getUseDuration(stack) - remainingUseDuration;

            if (ticksUsed == 2) {


                ClientChargeSound.start(
                        "steel_ball_spin",
                        ModSounds.SPINSOUND.get(),
                        true,
                        entity
                );
            }
        }
    }


    @Override
    public void releaseUsing(
            ItemStack stack,
            Level level,
            LivingEntity entity,
            //int timeLeft,
            int remainingUseDuration) {

        if (level.isClientSide) {
            net.minecraftforge.fml.DistExecutor.unsafeRunWhenOn(
                    net.minecraftforge.api.distmarker.Dist.CLIENT,
                    () -> () -> ClientChargeSound.stop("steel_ball_spin")
            );
        }


        int ticksUsed = getUseDuration(stack) - remainingUseDuration;

        //double maxDistance = 11 + (Math.max(0, ticksUsed - 11) * 8 / 46);
        double maxDistance = 2 + (Math.max(0, ticksUsed - 2) * 10 / 60);
        maxDistance = Math.min(maxDistance, 10);

        // Put your projectile-shooting code here.
        if (!level.isClientSide() && entity instanceof Player player) {

            WreckingBallProjectile projectile = new WreckingBallProjectile(level, player);

            projectile.setMaxDistance(maxDistance);

            // Set the projectile's initial position
            projectile.setPos(
                    player.getX(),
                    player.getEyeY() - 0.1,
                    player.getZ()
            );

            // Launch it in the direction the player is looking
            projectile.shootFromRotation(
                    player,
                    player.getXRot(),
                    player.getYRot(),
                    0.0F,
                    1.5F,
                    0.0F
            );

            // Add it to the world
            if(level.addFreshEntity(projectile))
            {
                stack.shrink(1);
            }
        }
    }



}
