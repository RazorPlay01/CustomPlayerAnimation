package com.github.razorplay01.cpa.animation.animations.base;

import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.records.AnimationContext;

import static com.github.razorplay01.cpa.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.cpa.CustomPlayerAnimations.getAnimation;

public class WalkAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.getMainAnimations().moveAnimations.walkingAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
        } else {
            context.mainAnimationContainer().setAnimationSpeed((float) (context.playerData().getMovementSpeed() * CONFIG.getMainAnimations().moveAnimations.getAnimationMoveSpeedMultiplier() * CONFIG.getMainAnimations().moveAnimations.walkingAnimationConfig.getSpeedMultiplier()));
            context.mainAnimationContainer().setAnimationFadeTime(CONFIG.getMainAnimations().moveAnimations.walkingAnimationConfig.getFadeTime());
            context.mainAnimationContainer().setAnimationPriority(CONFIG.getMainAnimations().moveAnimations.walkingAnimationConfig.getPriority());

            context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.WALK_ANIMATION.getAnimationId()));
            context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.WALK_ANIMATION.getAnimationId());
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.playerData().getMovementSpeed() > 0 &&
                !context.playerData().isMovingBackwards() &&
                !context.player().isCrouching() &&
                !context.player().isPassenger();
    }
}
