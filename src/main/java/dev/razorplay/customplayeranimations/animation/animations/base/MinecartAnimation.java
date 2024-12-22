package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.entity.vehicle.Minecart;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;

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

                    context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.MINECART_IDLE_ANIMATION.getAnimationId()));
                    context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.MINECART_IDLE_ANIMATION.getAnimationId());
                }
            }
        }
    }
}
