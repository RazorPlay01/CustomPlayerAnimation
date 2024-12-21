package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.entity.animal.horse.*;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.HORSE_IDLE_ANIMATION;

public class HorseRunningBackwardsAnimation {
    private HorseRunningBackwardsAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isPassenger()) {
            var vehicle = context.player().getVehicle();
            if ((vehicle instanceof Horse || vehicle instanceof SkeletonHorse || vehicle instanceof ZombieHorse || vehicle instanceof Donkey || vehicle instanceof Mule) && context.playerData().getMovementSpeed() > 0 && context.playerData().isMovingBackwards()) {
                if (!CONFIG.horseAnimationsConfig.horseRunningBackwardsAnimationConfig.isEnabled()) {
                    context.mainAnimationContainer().disableAnimation();
                } else {
                    context.mainAnimationContainer().setAnimationSpeed(CONFIG.horseAnimationsConfig.horseRunningBackwardsAnimationConfig.getSpeedMultiplier());
                    context.mainAnimationContainer().setAnimationFadeTime(CONFIG.horseAnimationsConfig.horseRunningBackwardsAnimationConfig.getFadeTime());
                    context.mainAnimationContainer().setAnimationPriority(CONFIG.horseAnimationsConfig.horseRunningBackwardsAnimationConfig.getPriority());

                    context.mainAnimationContainer().setCurrentAnimation(HORSE_IDLE_ANIMATION.animation());
                    context.mainAnimationContainer().setCurrentAnimationId(HORSE_IDLE_ANIMATION.animationId());
                }
            }

        }
    }
}
