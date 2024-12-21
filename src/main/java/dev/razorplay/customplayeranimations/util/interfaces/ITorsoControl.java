package dev.razorplay.customplayeranimations.util.interfaces;

import net.minecraft.client.model.HumanoidModel;

public interface ITorsoControl {
    void disableArmsAnimation(boolean disableArms);
    void disableRightArmAnimation(boolean disableRightArm);
    void disableLeftArmAnimation(boolean disableLeftArm);
    void setMainArmPose(HumanoidModel.ArmPose armPosen);
    void setOffArmPose(HumanoidModel.ArmPose armPose);
}

