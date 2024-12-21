package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.entity.vehicle.Minecart;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.MINECART_IDLE_ANIMATION;

public class MinecartAnimation {
    private MinecartAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isPassenger()) {
            var vehicle = context.player().getVehicle();
            if (vehicle instanceof Minecart) {
                if (!CONFIG.minecartAnimationsConfig.isEnabled()) {
                    context.mainAnimationContainer().disableAnimation();
                } else {
                    context.mainAnimationContainer().setAnimationSpeed(CONFIG.minecartAnimationsConfig.getSpeedMultiplier());
                    context.mainAnimationContainer().setAnimationFadeTime(CONFIG.minecartAnimationsConfig.getFadeTime());
                    context.mainAnimationContainer().setAnimationPriority(CONFIG.minecartAnimationsConfig.getPriority());

                    context.mainAnimationContainer().setCurrentAnimation(MINECART_IDLE_ANIMATION.animation());
                    context.mainAnimationContainer().setCurrentAnimationId(MINECART_IDLE_ANIMATION.animationId());
                }
            }
        }
    }
}
