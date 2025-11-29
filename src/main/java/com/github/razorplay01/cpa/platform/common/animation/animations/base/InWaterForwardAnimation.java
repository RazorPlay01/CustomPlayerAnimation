package com.github.razorplay01.cpa.platform.common.animation.animations.base;

import com.github.razorplay01.cpa.platform.common.animation.AnimationContainer;
import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.platform.common.util.Util.configureAnimationContainer;

public class InWaterForwardAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.getMainAnimations().inWaterAnimations.inWaterForwardAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
        } else {
            configureAnimationContainer(CONFIG.getMainAnimations().inWaterAnimations.inWaterForwardAnimationConfig, context.mainAnimationContainer());

			AnimationContainer.setAnimation(context.mainAnimationContainer(), AnimationsId.IN_WATER_FORWARD_ANIMATION);
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return (context.player().isUnderWater() || context.player().isInLava()) && !context.player().onGround() && !context.player().isVisuallySwimming() && (context.playerData().getMovementSpeed() > 0 && !context.playerData().isMovingBackwards());
    }
}
