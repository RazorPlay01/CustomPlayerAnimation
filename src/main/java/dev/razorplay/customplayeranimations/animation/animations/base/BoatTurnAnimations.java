package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.BOAT_TURN_LEFT_ANIMATION;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.BOAT_TURN_RIGHT_ANIMATION;

public class BoatTurnAnimations {
    private BoatTurnAnimations() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isPassenger()) {
            var vehicle = context.player().getVehicle();
            if (vehicle instanceof Boat || vehicle instanceof ChestBoat) {
                if (!CONFIG.boatAnimations.boatTurnAnimationConfig.isEnabled()) {
                    context.mainAnimationContainer().disableAnimation();
                } else {
                    boolean isLeftPaddleMoving = ((Boat) vehicle).getPaddleState(0);
                    boolean isRightPaddleMoving = ((Boat) vehicle).getPaddleState(1);

                    context.mainAnimationContainer().setAnimationSpeed(CONFIG.boatAnimations.boatTurnAnimationConfig.getSpeedMultiplier());
                    context.mainAnimationContainer().setAnimationFadeTime(CONFIG.boatAnimations.boatTurnAnimationConfig.getFadeTime());
                    context.mainAnimationContainer().setAnimationPriority(CONFIG.boatAnimations.boatTurnAnimationConfig.getPriority());

                    if (context.playerData().getMovementSpeed() > 0 && !context.playerData().isMovingBackwards()) {
                        if (isLeftPaddleMoving) {
                            context.mainAnimationContainer().setCurrentAnimation(BOAT_TURN_LEFT_ANIMATION.animation());
                            context.mainAnimationContainer().setCurrentAnimationId(BOAT_TURN_LEFT_ANIMATION.animationId());
                        } else if (isRightPaddleMoving) {
                            context.mainAnimationContainer().setCurrentAnimation(BOAT_TURN_RIGHT_ANIMATION.animation());
                            context.mainAnimationContainer().setCurrentAnimationId(BOAT_TURN_RIGHT_ANIMATION.animationId());
                        }
                    }
                }
            }
        }
    }
}