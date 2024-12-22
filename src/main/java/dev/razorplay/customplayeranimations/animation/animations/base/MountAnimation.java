package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;

public class MountAnimation {
    private MountAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isPassenger()) {
            context.mainAnimationContainer().setAnimationSpeed(1);
            context.mainAnimationContainer().setAnimationFadeTime(10);
            context.mainAnimationContainer().setAnimationPriority(0);

            context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.MINECART_IDLE_ANIMATION.getAnimationId()));
            context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.MINECART_IDLE_ANIMATION.getAnimationId());
        }
    }
}
