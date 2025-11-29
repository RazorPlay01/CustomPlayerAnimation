package com.github.razorplay01.cpa.platform.common.animation.animations.base;

import com.github.razorplay01.cpa.platform.common.animation.AnimationContainer;
import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;

public class OnFenceWalkAnimation implements ICustomAnimation {
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.getMainAnimations().moveAnimations.onFenceWalkAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
        } else {
            context.mainAnimationContainer().setAnimationSpeed((float) context.playerData().getMovementSpeed() * CONFIG.getMainAnimations().moveAnimations.getAnimationMoveSpeedMultiplier() * CONFIG.getMainAnimations().moveAnimations.onFenceWalkAnimationConfig.getSpeedMultiplier());
            context.mainAnimationContainer().setAnimationFadeTime(CONFIG.getMainAnimations().moveAnimations.onFenceWalkAnimationConfig.getFadeTime());
            context.mainAnimationContainer().setAnimationPriority(CONFIG.getMainAnimations().moveAnimations.onFenceWalkAnimationConfig.getPriority());

			AnimationContainer.setAnimation(context.mainAnimationContainer(), AnimationsId.ON_FENCE_WALK_ANIMATION);
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.playerData().getMovementSpeed() > 0
                && !context.playerData().isMovingBackwards() &&
                !context.player().isCrouching()
                && context.playerData().isOnFence()
                && !context.player().isPassenger();
    }
}
