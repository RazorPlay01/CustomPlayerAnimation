package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.InteractionHand;

public class GenericHandswingAnimation {
    private GenericHandswingAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().swinging) {
            disableArmBasedOnHand(context, context.player().swingingArm);
            context.overlayAnimationContainer().setCurrentAnimationId("handswinging" + context.overlayAnimationContainer().getCurrentAnimationId());
            context.overlayAnimationContainer().setAnimationFadeTime(0);
            context.overlayAnimationContainer().setAnimationPriority(0);
        }
    }

    private static void disableArmBasedOnHand(AnimationContext context, InteractionHand hand) {
        disableArmInBuilder(context, hand == context.playerData().getRightHand() ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);
    }

    private static void disableArmInBuilder(AnimationContext context, BodyParts arm) {
        KeyframeAnimation.AnimationBuilder builder = context.mainAnimationContainer().getCurrentAnimation().mutableCopy();
        var armPart = builder.getPart(arm.getPartId());
        if (armPart != null) {
            armPart.pitch.setEnabled(false);
            armPart.yaw.setEnabled(false);
            armPart.roll.setEnabled(false);
            armPart.setEnabled(false);
        }
        context.mainAnimationContainer().setCurrentAnimation(builder.build());

        builder = context.overlayAnimationContainer().getCurrentAnimation().mutableCopy();
        armPart = builder.getPart(arm.getPartId());
        if (armPart != null) {
            armPart.pitch.setEnabled(false);
            armPart.yaw.setEnabled(false);
            armPart.roll.setEnabled(false);
            armPart.setEnabled(false);
        }
        context.overlayAnimationContainer().setCurrentAnimation(builder.build());
    }
}
