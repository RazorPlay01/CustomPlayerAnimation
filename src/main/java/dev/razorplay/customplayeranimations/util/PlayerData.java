package dev.razorplay.customplayeranimations.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;

import static net.minecraft.world.InteractionHand.MAIN_HAND;
import static net.minecraft.world.InteractionHand.OFF_HAND;

@Getter
@Setter
@NoArgsConstructor
public class PlayerData {
    private Vec3 playerPosition;
    private Vec3 prevPlayerPosition = new Vec3(0, 0, 0);
    private double movementSpeed = 0;
    private float playerHeadYaw = 0;
    private float playerBodyYaw = 0;
    private float prevPlayerBodyYaw = 0;
    private float bodyYawDelta = 0;
    private float vectorX = 0;
    private float vectorY = 0;
    private float vectorZ = 0;
    private int flychecker = 0;
    private boolean isMovingBackwards = false;
    private boolean isOnFence = false;
    private boolean isOnEdge = false;
    private InteractionHand rightHand = MAIN_HAND;
    private InteractionHand leftHand = OFF_HAND;
    private HumanoidModel.ArmPose mainArmPose = HumanoidModel.ArmPose.EMPTY;
    private HumanoidModel.ArmPose offArmPose = HumanoidModel.ArmPose.EMPTY;
}
