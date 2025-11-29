package com.github.razorplay01.cpa.platform.common.animation.animations.base;

import com.github.razorplay01.cpa.platform.common.animation.AnimationContainer;
import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;

public class WalkBackwardsAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.getMainAnimations().moveAnimations.walkingBackwardsAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
        } else {
            context.mainAnimationContainer().setAnimationSpeed((float) (context.playerData().getMovementSpeed() * CONFIG.getMainAnimations().moveAnimations.getAnimationMoveSpeedMultiplier() * CONFIG.getMainAnimations().moveAnimations.walkingBackwardsAnimationConfig.getSpeedMultiplier()));
            context.mainAnimationContainer().setAnimationFadeTime(CONFIG.getMainAnimations().moveAnimations.walkingBackwardsAnimationConfig.getFadeTime());
            context.mainAnimationContainer().setAnimationPriority(CONFIG.getMainAnimations().moveAnimations.walkingBackwardsAnimationConfig.getPriority());

			AnimationContainer.setAnimation(context.mainAnimationContainer(), AnimationsId.WALK_BACKWARDS_ANIMATION);
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.playerData().getMovementSpeed() > 0 && context.playerData().isMovingBackwards() && !context.player().isCrouching() && !context.player().isPassenger();
    }
}
