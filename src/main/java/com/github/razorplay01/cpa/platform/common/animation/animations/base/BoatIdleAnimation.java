package com.github.razorplay01.cpa.platform.common.animation.animations.base;

import com.github.razorplay01.cpa.platform.common.animation.AnimationContainer;
import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.platform.common.util.Util.configureAnimationContainer;
import static com.github.razorplay01.cpa.platform.common.util.Util.isBoat;

public class BoatIdleAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        var vehicle = context.player().getVehicle();
        if (isBoat(vehicle)) {
            if (!CONFIG.getMainAnimations().mountAnimations.boatAnimations.boatIdleAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
                return;
            }
            configureAnimationContainer(CONFIG.getMainAnimations().mountAnimations.boatAnimations.boatIdleAnimationConfig, context.mainAnimationContainer());

			AnimationContainer.setAnimation(context.mainAnimationContainer(), AnimationsId.BOAT_IDLE_ANIMATION);
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().isPassenger() && context.playerData().getMovementSpeed() == 0 || context.playerData().isMovingBackwards();
    }
}
