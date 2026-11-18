package net.chargablepipe.keeplooking4ward.item.custom;

import net.chargablepipe.keeplooking4ward.client.ClientChargeSound;
import net.chargablepipe.keeplooking4ward.entity.custom.SteelBallProjectile;
import net.chargablepipe.keeplooking4ward.sound.ModSounds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
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

            if (ticksUsed == 11) {
                net.minecraftforge.fml.DistExecutor.unsafeRunWhenOn(
                        net.minecraftforge.api.distmarker.Dist.CLIENT,
                        () -> () -> ClientChargeSound.start(
                                ModSounds.SPINSOUND.get(),
                                LOOP_SOUND,
                                entity
                        )
                );
                //if(entity instanceof Player player)
                //{
                    //level.playSeededSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.SPINSOUND);
                //}
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

    //@Override
    //public UseAnim getUseAnimation(ItemStack p_41452_) {
        //return UseAnim.BOW;
    //}

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

        int ticksUsed = getUseDuration(stack) - remainingUseDuration;

        //double maxDistance = 11 + (Math.max(0, ticksUsed - 11) * 8 / 46);
        double maxDistance = 11 + (Math.max(0, ticksUsed - 11) * 10 / 60);
        maxDistance = Math.min(maxDistance, 21);

        // Put your projectile-shooting code here.
        if (!level.isClientSide() && entity instanceof Player player) {

            SteelBallProjectile projectile = new SteelBallProjectile(level, player);

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
