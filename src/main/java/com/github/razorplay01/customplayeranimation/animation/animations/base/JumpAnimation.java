package com.github.razorplay01.customplayeranimation.animation.animations.base;

import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;

import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.getAnimation;

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
