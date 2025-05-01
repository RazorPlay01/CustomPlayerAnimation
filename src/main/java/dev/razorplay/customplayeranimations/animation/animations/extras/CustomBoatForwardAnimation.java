package dev.razorplay.customplayeranimations.animation.animations.extras;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.entity.vehicle.Boat;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.configureAnimationContainer;
import static dev.razorplay.customplayeranimations.util.Util.isBoat;

public class CustomBoatForwardAnimation implements ICustomAnimation {
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

        if (!CONFIG.mountAnimations.boatAnimations.boatForwardAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
            return;
        }
        configureAnimationContainer(CONFIG.mountAnimations.boatAnimations.boatForwardAnimationConfig, context.mainAnimationContainer());

        boolean isLeftPaddleMoving = boat.getPaddleState(0);
        boolean isRightPaddleMoving = boat.getPaddleState(1);

        if (shouldPlayAnimation(context, isLeftPaddleMoving, isRightPaddleMoving)) {
            context.mainAnimationContainer().setCurrentAnimation(getAnimation("custom_boat_forward_animation"));
            context.mainAnimationContainer().setCurrentAnimationId("custom_boat_forward_animation");
        }
    }

    private static boolean shouldPlayAnimation(AnimationContext context, boolean isLeftPaddleMoving, boolean isRightPaddleMoving) {
        var vehicle = context.player().getVehicle();
        if (!isBoat(vehicle)) {
            return false;
        }
        Boat boat = (Boat) vehicle;
        Boat.Type boatType = boat.getVariant();
        return context.playerData().getMovementSpeed() > 0 &&
                !context.playerData().isMovingBackwards() &&
                isLeftPaddleMoving &&
                isRightPaddleMoving &&
                boatType == Boat.Type.OAK;
    }
}