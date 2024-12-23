package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.CrossbowItem;

import static dev.razorplay.customplayeranimations.util.Util.disableArmInBuilder;

public class CrossbowAnimation {
    private CrossbowAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isUsingItem() && context.player().getUseItem().getItem() instanceof CrossbowItem) {
            disableArmInBuilder(context, BodyParts.RIGHT_ARM);
            disableArmInBuilder(context, BodyParts.LEFT_ARM);
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
}
