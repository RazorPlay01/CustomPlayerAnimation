package com.github.razorplay01.customplayeranimation.animation.animations.overlay;

import com.github.razorplay01.customplayeranimation.util.enums.BodyParts;
import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.CrossbowItem;

import static com.github.razorplay01.customplayeranimation.util.Util.disableBothArms;

public class CrossbowAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        // Handle charging (both arms disabled)
        if (context.player().isUsingItem() && context.player().getUseItem().getItem() instanceof CrossbowItem) {
            disableBothArms(context);
            return; // Skip pose checks during charging
        }
        // Handle holding (disable only the arm holding the crossbow)
        if (context.playerData().getMainArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_HOLD)) {
            context.player().disableBodyPartAnimationInAllContainers(
                    context.player().getMainArm() == HumanoidArm.RIGHT ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM
            );
        }
        if (context.playerData().getOffArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_HOLD)) {
            context.player().disableBodyPartAnimationInAllContainers(
                    context.player().getMainArm() == HumanoidArm.RIGHT ? BodyParts.LEFT_ARM : BodyParts.RIGHT_ARM
            );
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        // Only play if player is holding or using a crossbow
        return context.player().getMainHandItem().getItem() instanceof CrossbowItem ||
                context.player().getOffhandItem().getItem() instanceof CrossbowItem;
    }
}
