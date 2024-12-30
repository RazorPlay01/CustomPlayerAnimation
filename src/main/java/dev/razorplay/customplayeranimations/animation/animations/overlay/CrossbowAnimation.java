package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.CrossbowItem;

import static dev.razorplay.customplayeranimations.util.Util.disableBothArms;

public class CrossbowAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (context.player().isUsingItem() && context.player().getUseItem().getItem() instanceof CrossbowItem) {
            disableBothArms(context);
        }
        if (context.playerData().getMainArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_HOLD) ||
                context.playerData().getMainArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_CHARGE)) {
            context.player().disableBodyPartAnimationInAllContainers(context.player().getMainArm() == HumanoidArm.RIGHT ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);
        }
        if (context.playerData().getOffArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_HOLD) ||
                context.playerData().getOffArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_CHARGE)) {
            context.player().disableBodyPartAnimationInAllContainers(context.player().getMainArm() == HumanoidArm.RIGHT ? BodyParts.LEFT_ARM : BodyParts.RIGHT_ARM);
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return true;
    }
}
