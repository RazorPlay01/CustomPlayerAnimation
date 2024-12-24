package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.animation.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;

public class WalkAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (shouldPlayAnimation(context)) {
            if (!CONFIG.moveAnimations.walkingAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                context.mainAnimationContainer().setAnimationSpeed((float) (context.playerData().getMovementSpeed() * CONFIG.moveAnimations.getAnimationMoveSpeedMultiplier() * CONFIG.moveAnimations.walkingAnimationConfig.getSpeedMultiplier()));
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.moveAnimations.walkingAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.moveAnimations.walkingAnimationConfig.getPriority());

                context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.WALKING_ANIMATION.getAnimationId()));
                context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.WALKING_ANIMATION.getAnimationId());
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.playerData().getMovementSpeed() > 0 && !context.playerData().isMovingBackwards() && !context.player().isCrouching() && !context.player().isPassenger();
    }
}
