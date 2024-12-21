package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.entity.vehicle.Minecart;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.HORSE_IDLE_ANIMATION;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.MINECART_IDLE_ANIMATION;

public class MountAnimation {
    private MountAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isPassenger()) {
            context.mainAnimationContainer().setAnimationSpeed(1);
            context.mainAnimationContainer().setAnimationFadeTime(10);
            context.mainAnimationContainer().setAnimationPriority(0);

            context.mainAnimationContainer().setCurrentAnimation(MINECART_IDLE_ANIMATION.animation());
            context.mainAnimationContainer().setCurrentAnimationId(MINECART_IDLE_ANIMATION.animationId());
        }
    }
}
