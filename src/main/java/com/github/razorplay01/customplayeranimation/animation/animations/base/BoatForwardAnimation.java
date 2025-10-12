package com.github.razorplay01.customplayeranimation.animation.animations.base;

import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;
import net.minecraft.world.entity.vehicle.Boat;

import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.customplayeranimation.util.Util.configureAnimationContainer;
import static com.github.razorplay01.customplayeranimation.util.Util.isBoat;

public class BoatForwardAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        handleBoatAnimation(context);
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        var vehicle = context.player().getVehicle();
        return context.player().isPassenger() && isBoat(vehicle);
    }

    private static void handleBoatAnimation(AnimationContext context) {
        Boat boat = (Boat) context.player().getVehicle();

        if (!CONFIG.getMainAnimations().mountAnimations.boatAnimations.boatForwardAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
            return;
        }
        configureAnimationContainer(CONFIG.getMainAnimations().mountAnimations.boatAnimations.boatForwardAnimationConfig, context.mainAnimationContainer());

        boolean isLeftPaddleMoving = boat.getPaddleState(0);
        boolean isRightPaddleMoving = boat.getPaddleState(1);

        if (shouldPlayAnimation(context, isLeftPaddleMoving, isRightPaddleMoving)) {
            context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.BOAT_FORWARD_ANIMATION.getAnimationId()));
            context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.BOAT_FORWARD_ANIMATION.getAnimationId());
        }
    }

    private static boolean shouldPlayAnimation(AnimationContext context, boolean isLeftPaddleMoving, boolean isRightPaddleMoving) {
        return context.playerData().getMovementSpeed() > 0 && !context.playerData().isMovingBackwards() && isLeftPaddleMoving && isRightPaddleMoving;
    }
}