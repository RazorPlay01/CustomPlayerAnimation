package dev.razorplay.customplayeranimations.util.interfaces;

import dev.razorplay.customplayeranimations.animation.AnimationContainer;
import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import net.minecraft.client.model.HumanoidModel;

public interface IAnimationControl {
    void disableActiveArm(AnimationContainer animationContainer);

    void disableBodyPartAnimation(AnimationContainer animationContainer, BodyParts bodyPart);

    void disableBodyPartAnimationInAllContainers(BodyParts bodyParts);

    void setMainArmPose(HumanoidModel.ArmPose armPose);

    void setOffArmPose(HumanoidModel.ArmPose armPose);
}

