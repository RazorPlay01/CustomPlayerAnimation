package com.github.razorplay01.customplayeranimation.animation.animations.base;

import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;

import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.customplayeranimation.util.Util.configureAnimationContainer;
import static com.github.razorplay01.customplayeranimation.util.Util.isHorse;

public class HorseRunningAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.mountAnimations.horseAnimationsConfig.horseRunningAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
        } else {
            configureAnimationContainer(CONFIG.mountAnimations.horseAnimationsConfig.horseRunningAnimationConfig, context.mainAnimationContainer());

            context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.HORSE_RUN_ANIMATION.getAnimationId()));
            context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.HORSE_RUN_ANIMATION.getAnimationId());
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        var vehicle = context.player().getVehicle();
        return context.player().isPassenger() && isHorse(vehicle) && context.playerData().getMovementSpeed() > 0 && !context.playerData().isMovingBackwards();
    }
}
