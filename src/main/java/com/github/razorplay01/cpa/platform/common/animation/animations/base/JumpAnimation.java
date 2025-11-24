package com.github.razorplay01.cpa.platform.common.animation.animations.base;

import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;

import static com.github.razorplay01.cpa.ModTemplate.getAnimation;

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
