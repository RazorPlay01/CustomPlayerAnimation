package dev.razorplay.customplayeranimations.util.interfaces;

import net.minecraft.client.model.HumanoidModel;

public interface IAnimationControl {
    void setMainArmPose(HumanoidModel.ArmPose armPosen);
    void setOffArmPose(HumanoidModel.ArmPose armPose);
}

