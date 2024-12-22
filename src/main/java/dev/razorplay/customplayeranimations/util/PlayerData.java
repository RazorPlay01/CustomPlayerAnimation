package dev.razorplay.customplayeranimations.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import static java.lang.Math.*;
import static java.lang.Math.sin;
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
    private boolean prevOnGround = false;
    private InteractionHand rightHand = MAIN_HAND;
    private InteractionHand leftHand = OFF_HAND;
    private HumanoidModel.ArmPose mainArmPose = HumanoidModel.ArmPose.EMPTY;
    private HumanoidModel.ArmPose offArmPose = HumanoidModel.ArmPose.EMPTY;

    public void updateHandOrientation(HumanoidArm mainHand) {
        if (mainHand == HumanoidArm.RIGHT) {
            setRightHand(MAIN_HAND);
            setLeftHand(OFF_HAND);
        } else {
            setRightHand(OFF_HAND);
            setLeftHand(MAIN_HAND);
        }
    }

    public void updatePlayerPosition(float headYRot, float visualRotationYInDegress, Vec3 playerPosition) {
        setPlayerBodyYaw(visualRotationYInDegress);
        setPlayerHeadYaw(headYRot);
        setPlayerPosition(playerPosition);
        setVectorX((float) (getPlayerPosition().x - getPrevPlayerPosition().x));
        setVectorY((float) (getPlayerPosition().y - getPrevPlayerPosition().y));
        setVectorZ((float) (getPlayerPosition().z - getPrevPlayerPosition().z));
        setMovementSpeed(sqrt(getVectorX() * getVectorX() + getVectorZ() * getVectorZ()));
        setBodyYawDelta(getPlayerBodyYaw() - getPrevPlayerBodyYaw());
    }

    public void updateMovementInfo(float yBodyRot) {
        double bodyYawRadians = toRadians(yBodyRot + 90);
        Vector3f movementVector = new Vector3f(getVectorX(), 0, getVectorZ());
        Vector3f lookVector = new Vector3f((float) cos(bodyYawRadians), 0, (float) sin(bodyYawRadians));
        setMovingBackwards(movementVector.length() > 0 && movementVector.dot(lookVector) < 0);
    }

    public void updateEnvironmentInfo(AbstractClientPlayer player) {
        Block standingBlock = player.level().getBlockState(player.blockPosition().below()).getBlock();
        setOnFence((standingBlock instanceof FenceBlock || standingBlock instanceof WallBlock || standingBlock instanceof IronBarsBlock) && player.onGround());
        setOnEdge(standingBlock instanceof AirBlock && player.onGround());
    }
}
