package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.CrossbowItem;

public class CrossbowAnimation {
    private CrossbowAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isUsingItem() && context.player().getUseItem().getItem() instanceof CrossbowItem) {
            disableBothArms(context);
        }
        if (context.playerData().getMainArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_HOLD) ||
                context.playerData().getMainArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_CHARGE)) {
            disableArmInBuilder(context, context.player().getMainArm() == HumanoidArm.RIGHT ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);
        }
        if (context.playerData().getOffArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_HOLD) ||
                context.playerData().getOffArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_CHARGE)) {
            disableArmInBuilder(context, context.player().getMainArm() == HumanoidArm.RIGHT ? BodyParts.LEFT_ARM : BodyParts.RIGHT_ARM);
        }
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

    private static void disableBothArms(AnimationContext context) {
        disableArmInBuilder(context, BodyParts.RIGHT_ARM);
        disableArmInBuilder(context, BodyParts.LEFT_ARM);
    }
}
