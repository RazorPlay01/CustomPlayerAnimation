package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.animation.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.configureAnimationContainer;
import static dev.razorplay.customplayeranimations.util.Util.isBoat;

public class BoatIdleAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!context.player().isPassenger()) {
            return;
        }
        var vehicle = context.player().getVehicle();
        if (isBoat(vehicle)) {
            if (!CONFIG.mountAnimations.boatAnimations.boatIdleAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
                return;
            }
            configureAnimationContainer(CONFIG.mountAnimations.boatAnimations.boatIdleAnimationConfig, context.mainAnimationContainer());

            if (shouldPlayAnimation(context)) {
                context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.BOAT_IDLE_ANIMATION.getAnimationId()));
                context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.BOAT_IDLE_ANIMATION.getAnimationId());
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.playerData().getMovementSpeed() == 0 || context.playerData().isMovingBackwards();
    }
}