package dev.razorplay.customplayeranimations.util;

import net.minecraft.client.model.HumanoidModel;

public interface ITorsoControl {
    void disableArmsAnimation(boolean b);
    void setMainArmPosition(HumanoidModel.ArmPose pos);
    void setOffArmPosition(HumanoidModel.ArmPose pos);
}

