package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;

public class JumpAnimation {
    private JumpAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (!context.player().onGround() && context.playerData().isPrevOnGround() && context.playerData().getVectorY() > 0) {
            context.mainAnimationContainer().setAnimationSpeed(0.2f);
            context.mainAnimationContainer().setAnimationFadeTime(3);
            context.mainAnimationContainer().setAnimationPriority(0);

            context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.JUMP_ANIMATION.getAnimationId()));
            context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.JUMP_ANIMATION.getAnimationId());
        }
    }
}
