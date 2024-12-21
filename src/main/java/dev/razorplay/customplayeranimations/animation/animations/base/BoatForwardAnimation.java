package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.BOAT_FORWARD_ANIMATION;

public class BoatForwardAnimation {
    private BoatForwardAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isPassenger()) {
            var vehicle = context.player().getVehicle();
            if (vehicle instanceof Boat || vehicle instanceof ChestBoat) {
                if (!CONFIG.boatAnimations.boatForwardAnimationConfig.isEnabled()) {
                    context.mainAnimationContainer().disableAnimation();
                } else {
                    boolean isLeftPaddleMoving = ((Boat) vehicle).getPaddleState(0);
                    boolean isRightPaddleMoving = ((Boat) vehicle).getPaddleState(1);

                    context.mainAnimationContainer().setAnimationSpeed(CONFIG.boatAnimations.boatForwardAnimationConfig.getSpeedMultiplier());
                    context.mainAnimationContainer().setAnimationFadeTime(CONFIG.boatAnimations.boatForwardAnimationConfig.getFadeTime());
                    context.mainAnimationContainer().setAnimationPriority(CONFIG.boatAnimations.boatForwardAnimationConfig.getPriority());

                    if (context.playerData().getMovementSpeed() > 0 && !context.playerData().isMovingBackwards() && isLeftPaddleMoving && isRightPaddleMoving) {
                            context.mainAnimationContainer().setCurrentAnimation(BOAT_FORWARD_ANIMATION.animation());
                            context.mainAnimationContainer().setCurrentAnimationId(BOAT_FORWARD_ANIMATION.animationId());
                        }
                }
            }
        }
    }
}