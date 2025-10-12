package com.github.razorplay01.cpa.util.interfaces;

import com.github.razorplay01.cpa.animation.AnimationContainer;
import com.github.razorplay01.cpa.util.enums.BodyParts;
import com.github.razorplay01.cpa.util.records.AnimationContext;
import net.minecraft.client.model.HumanoidModel;

public interface IAnimationControl {
    AnimationContext getAnimationContext();

    void disableActiveArm(AnimationContainer animationContainer);

    void disableBodyPartAnimation(AnimationContainer animationContainer, BodyParts bodyPart);

    void disableBodyPartAnimationInAllContainers(BodyParts bodyParts);

    void setMainArmPose(HumanoidModel.ArmPose armPose);

    void setOffArmPose(HumanoidModel.ArmPose armPose);

    HumanoidModel.ArmPose getMainArmPose();

    HumanoidModel.ArmPose getOffArmPose();
}

