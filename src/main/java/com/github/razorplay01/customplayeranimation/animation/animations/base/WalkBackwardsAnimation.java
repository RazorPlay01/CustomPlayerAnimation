package com.github.razorplay01.customplayeranimation.animation.animations.base;

import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;

import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.getAnimation;

public class WalkBackwardsAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.moveAnimations.walkingBackwardsAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
        } else {
            context.mainAnimationContainer().setAnimationSpeed((float) (context.playerData().getMovementSpeed() * CONFIG.moveAnimations.getAnimationMoveSpeedMultiplier() * CONFIG.moveAnimations.walkingBackwardsAnimationConfig.getSpeedMultiplier()));
            context.mainAnimationContainer().setAnimationFadeTime(CONFIG.moveAnimations.walkingBackwardsAnimationConfig.getFadeTime());
            context.mainAnimationContainer().setAnimationPriority(CONFIG.moveAnimations.walkingBackwardsAnimationConfig.getPriority());

            context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.WALK_BACKWARDS_ANIMATION.getAnimationId()));
            context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.WALK_BACKWARDS_ANIMATION.getAnimationId());
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.playerData().getMovementSpeed() > 0 && context.playerData().isMovingBackwards() && !context.player().isCrouching() && !context.player().isPassenger();
    }
}
