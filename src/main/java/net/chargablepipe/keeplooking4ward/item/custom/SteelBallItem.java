package net.chargablepipe.keeplooking4ward.item.custom;

import net.chargablepipe.keeplooking4ward.client.ClientChargeSound;
import net.chargablepipe.keeplooking4ward.entity.custom.SteelBallProjectile;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;


public class SteelBallItem extends Item {
    public SteelBallItem(Properties p_41383_) {
        super(p_41383_);
    }

    private static final boolean LOOP_SOUND = true;

    @Override
    public void onUseTick(
            Level level,
            LivingEntity entity,
            ItemStack stack,
            int remainingUseDuration
    )
    {
        if (level.isClientSide) {
            int ticksUsed = getUseDuration(stack) - remainingUseDuration;

            if (ticksUsed == 14) {
                net.minecraftforge.fml.DistExecutor.unsafeRunWhenOn(
                        net.minecraftforge.api.distmarker.Dist.CLIENT,
                        () -> () -> ClientChargeSound.start(
                                SoundEvents.EXPERIENCE_ORB_PICKUP,
                                LOOP_SOUND,
                                entity
                        )
                );
            }
        }
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

    @Override
    public void releaseUsing(
            ItemStack stack,
            Level level,
            LivingEntity entity,
            int timeLeft) {


        if (level.isClientSide) {
            net.minecraftforge.fml.DistExecutor.unsafeRunWhenOn(
                    net.minecraftforge.api.distmarker.Dist.CLIENT,
                    () -> () -> ClientChargeSound.stop()
            );
        }

        if (!level.isClientSide) {
            level.playSound(
                    null,
                    entity.getX(),
                    entity.getY(),
                    entity.getZ(),
                    SoundEvents.ANVIL_LAND,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );
        }

        // Put your projectile-shooting code here.
        if (!level.isClientSide() && entity instanceof Player player) {

            SteelBallProjectile projectile = new SteelBallProjectile(level, player);

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
