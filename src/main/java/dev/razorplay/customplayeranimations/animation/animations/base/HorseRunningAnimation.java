package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.entity.animal.horse.*;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.HORSE_IDLE_ANIMATION;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.HORSE_RUNNING_ANIMATION;

public class HorseRunningAnimation {
    private HorseRunningAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isPassenger()) {
            var vehicle = context.player().getVehicle();
            if ((vehicle instanceof Horse || vehicle instanceof SkeletonHorse || vehicle instanceof ZombieHorse || vehicle instanceof Donkey || vehicle instanceof Mule) && context.playerData().getMovementSpeed() > 0 && !context.playerData().isMovingBackwards()) {
                if (!CONFIG.horseAnimationsConfig.horseRunningAnimationConfig.isEnabled()) {
                    context.mainAnimationContainer().disableAnimation();
                } else {
                    context.mainAnimationContainer().setAnimationSpeed(CONFIG.horseAnimationsConfig.horseRunningAnimationConfig.getSpeedMultiplier());
                    context.mainAnimationContainer().setAnimationFadeTime(CONFIG.horseAnimationsConfig.horseRunningAnimationConfig.getFadeTime());
                    context.mainAnimationContainer().setAnimationPriority(CONFIG.horseAnimationsConfig.horseRunningAnimationConfig.getPriority());

                    context.mainAnimationContainer().setCurrentAnimation(HORSE_RUNNING_ANIMATION.animation());
                    context.mainAnimationContainer().setCurrentAnimationId(HORSE_RUNNING_ANIMATION.animationId());
                }
            }

        }
    }
}
