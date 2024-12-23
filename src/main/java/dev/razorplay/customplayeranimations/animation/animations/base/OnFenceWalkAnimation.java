package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;

public class OnFenceWalkAnimation {
    private OnFenceWalkAnimation() {
        //[]
    }

    public static void playAnimation(AnimationContext context) {
        if (context.playerData().getMovementSpeed() > 0 && !context.playerData().isMovingBackwards() && !context.player().isCrouching() && context.playerData().isOnFence()) {
            if (!CONFIG.moveAnimations.onFenceWalkAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                context.mainAnimationContainer().setAnimationSpeed((float) context.playerData().getMovementSpeed() * CONFIG.getAnimationMoveSpeedMultiplier() * CONFIG.moveAnimations.onFenceWalkAnimationConfig.getSpeedMultiplier());
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.moveAnimations.onFenceWalkAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.moveAnimations.onFenceWalkAnimationConfig.getPriority());

                context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.ON_FENCE_WALKING_ANIMATION.getAnimationId()));
                context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.ON_FENCE_WALKING_ANIMATION.getAnimationId());
            }
        }
    }
}
