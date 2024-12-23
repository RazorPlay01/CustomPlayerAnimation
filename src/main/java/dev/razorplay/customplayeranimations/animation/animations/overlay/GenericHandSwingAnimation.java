package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.InteractionHand;

import static dev.razorplay.customplayeranimations.util.Util.disableBodyPart;

public class GenericHandSwingAnimation {
    private GenericHandSwingAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().swinging) {
            disableArmBasedOnHand(context, context.player().swingingArm);
            context.overlayAnimationContainer().setCurrentAnimationId("hand_swing" + context.overlayAnimationContainer().getCurrentAnimationId());
            context.overlayAnimationContainer().setAnimationFadeTime(0);
            context.overlayAnimationContainer().setAnimationPriority(0);
        }
    }

    private static void disableArmBasedOnHand(AnimationContext context, InteractionHand hand) {
        disableBodyPart(context.mainAnimationContainer(), hand == context.playerData().getRightHand() ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);
        disableBodyPart(context.overlayAnimationContainer(), hand == context.playerData().getRightHand() ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);
        disableBodyPart(context.upHandAnimationContainer(), hand == context.playerData().getRightHand() ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);
    }
}
