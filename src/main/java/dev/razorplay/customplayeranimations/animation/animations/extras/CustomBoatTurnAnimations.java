package dev.razorplay.customplayeranimations.animation.animations.extras;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.entity.vehicle.Boat;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.configureAnimationContainer;
import static dev.razorplay.customplayeranimations.util.Util.isBoat;

public class CustomBoatTurnAnimations implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        var vehicle = context.player().getVehicle();
        if (isBoat(vehicle)) {
            if (!CONFIG.mountAnimations.boatAnimations.boatTurnAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
                return;
            }

            configureAnimationContainer(CONFIG.mountAnimations.boatAnimations.boatTurnAnimationConfig, context.mainAnimationContainer());

            playBoatTurnAnimation(context, (Boat) vehicle);
        }
    }

    public boolean shouldPlayAnimation(AnimationContext context) {
        var vehicle = context.player().getVehicle();
        if (!isBoat(vehicle)) {
            return false;
        }
        Boat boat = (Boat) vehicle;
        Boat.Type boatType = boat.getVariant();
        return context.player().isPassenger() &&
                context.playerData().getMovementSpeed() > 0 &&
                !context.playerData().isMovingBackwards() &&
                boatType == Boat.Type.OAK;
    }

    private static void playBoatTurnAnimation(AnimationContext context, Boat boat) {
        boolean isLeftPaddleMoving = boat.getPaddleState(0);
        boolean isRightPaddleMoving = boat.getPaddleState(1);

        if (isLeftPaddleMoving) {
            context.mainAnimationContainer().setCurrentAnimation(getAnimation("custom_boat_turn_left_animation"));
            context.mainAnimationContainer().setCurrentAnimationId("custom_boat_turn_left_animation");
        } else if (isRightPaddleMoving) {
            context.mainAnimationContainer().setCurrentAnimation(getAnimation("custom_boat_turn_right_animation"));
            context.mainAnimationContainer().setCurrentAnimationId("custom_boat_turn_right_animation");
        }
    }
}