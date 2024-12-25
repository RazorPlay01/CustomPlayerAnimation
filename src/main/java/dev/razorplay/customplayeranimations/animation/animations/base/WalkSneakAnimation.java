package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;

public class WalkSneakAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.moveAnimations.walkingSneakAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
            return;
        }

        configureWalkSneakAnimation(context);
        setWalkSneakAnimation(context);
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.playerData().getMovementSpeed() > 0
                && !context.playerData().isMovingBackwards()
                && context.player().isCrouching() && !context.player().isPassenger();
    }

    private static void configureWalkSneakAnimation(AnimationContext context) {
        var animationContainer = context.mainAnimationContainer();
        var config = CONFIG.moveAnimations.walkingSneakAnimationConfig;

        animationContainer.setAnimationSpeed(calculateAnimationSpeed(context));
        animationContainer.setAnimationFadeTime(config.getFadeTime());
        animationContainer.setAnimationPriority(config.getPriority());
    }

    private static float calculateAnimationSpeed(AnimationContext context) {
        return (float) (5 * context.playerData().getMovementSpeed()
                * CONFIG.moveAnimations.getAnimationMoveSpeedMultiplier()
                * CONFIG.moveAnimations.walkingSneakAnimationConfig.getSpeedMultiplier());
    }

    private static void setWalkSneakAnimation(AnimationContext context) {
        context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.WALK_SNEAK_ANIMATION.getAnimationId()));
        context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.WALK_SNEAK_ANIMATION.getAnimationId());
    }
}
