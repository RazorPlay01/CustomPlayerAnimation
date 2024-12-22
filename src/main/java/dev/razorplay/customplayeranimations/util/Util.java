package dev.razorplay.customplayeranimations.util;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.razorplay.customplayeranimations.animation.AnimationContainer;
import dev.razorplay.customplayeranimations.config.ClientConfig;
import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.vehicle.Boat;

import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class Util {
    public static final String RIGHT_PREFIX = "right_";
    public static final String LEFT_PREFIX = "left_";

    private Util() {
        //[]
    }

    public static boolean isBoat(Object vehicle) {
        return vehicle instanceof Boat;
    }

    public static boolean isHorse(Object vehicle) {
        return vehicle instanceof Horse || vehicle instanceof SkeletonHorse || vehicle instanceof ZombieHorse || vehicle instanceof Donkey || vehicle instanceof Mule;
    }

    public static void configureAnimationContainer(ClientConfig.AnimationConfig config,
                                                   AnimationContainer animationContainer) {
        animationContainer.setAnimationSpeed(config.getSpeedMultiplier());
        animationContainer.setAnimationFadeTime(config.getFadeTime());
        animationContainer.setAnimationPriority(config.getPriority());
    }

    public static void disableBodyPart(AnimationContainer animationContainer, BodyParts bodyPart) {
        KeyframeAnimation.AnimationBuilder internalBuilder = animationContainer.getCurrentAnimation().mutableCopy();
        var part = internalBuilder.getPart(bodyPart.getPartId());
        if (part != null) {
            part.setEnabled(false);
        }
        animationContainer.setCurrentAnimation(internalBuilder.build());
    }

    public static void disableActiveArm(AnimationContext context, AnimationContainer animationContainer) {
        if (context.player().getUsedItemHand().equals(MAIN_HAND)) {
            disableBodyPart(animationContainer, context.player().getMainArm() == HumanoidArm.RIGHT ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);
        } else {
            disableBodyPart(animationContainer, context.player().getMainArm() == HumanoidArm.RIGHT ? BodyParts.LEFT_ARM : BodyParts.RIGHT_ARM);
        }
    }

    public static void disableArmInBuilder(AnimationContainer mainAnimationContainer, AnimationContainer overlayAnimationContainer, AnimationContainer upHandAnimationContainer, BodyParts arm) {
        disableBodyPart(mainAnimationContainer, arm);
        disableBodyPart(overlayAnimationContainer, arm);
        disableBodyPart(upHandAnimationContainer, arm);
    }

    public static void disableArmInBuilder(AnimationContext context, BodyParts arm) {
        disableBodyPart(context.mainAnimationContainer(), arm);
        disableBodyPart(context.overlayAnimationContainer(), arm);
        disableBodyPart(context.upHandAnimationContainer(), arm);
    }
}
