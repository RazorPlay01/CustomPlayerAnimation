package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.razorplay.customplayeranimations.util.enums.ArmsEnum;
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
            disableArmInBuilder(context, context.player().getMainArm() == HumanoidArm.RIGHT ? ArmsEnum.RIGHT_ARM : ArmsEnum.LEFT_ARM);
        }
        if (context.playerData().getOffArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_HOLD) ||
                context.playerData().getOffArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_CHARGE)) {
            disableArmInBuilder(context, context.player().getMainArm() == HumanoidArm.RIGHT ? ArmsEnum.LEFT_ARM : ArmsEnum.RIGHT_ARM);
        }
    }

    private static void disableArmInBuilder(AnimationContext context, ArmsEnum arm) {
        KeyframeAnimation.AnimationBuilder builder = context.mainAnimationContainer().getCurrentAnimation().mutableCopy();
        var armPart = builder.getPart(arm.getArmId());
        if (armPart != null) {
            armPart.pitch.setEnabled(false);
            armPart.yaw.setEnabled(false);
            armPart.roll.setEnabled(false);
            armPart.setEnabled(false);
        }
        context.mainAnimationContainer().setCurrentAnimation(builder.build());

        builder = context.overlayAnimationContainer().getCurrentAnimation().mutableCopy();
        armPart = builder.getPart(arm.getArmId());
        if (armPart != null) {
            armPart.pitch.setEnabled(false);
            armPart.yaw.setEnabled(false);
            armPart.roll.setEnabled(false);
            armPart.setEnabled(false);
        }
        context.overlayAnimationContainer().setCurrentAnimation(builder.build());
    }

    private static void disableBothArms(AnimationContext context) {
        disableArmInBuilder(context, ArmsEnum.RIGHT_ARM);
        disableArmInBuilder(context, ArmsEnum.LEFT_ARM);
    }
}
