package com.github.razorplay01.cpa.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.*;
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
    private ItemStack mainHandItem = ItemStack.EMPTY;
    private ItemStack offHandItem = ItemStack.EMPTY;
    private int movementTicks;
    private boolean prevMainHandUp = false;
    private boolean prevOffHandUp = false;

    public void update(AbstractClientPlayer player) {
        if (player == null) return;
        updateHandOrientation(player.getMainArm());
        updatePlayerPosition(player.getYHeadRot(), player.getVisualRotationYInDegrees(), player.position());
        updateMovementInfo(player.yBodyRot);
        updateEnvironmentInfo(player);
        updateFlyChecker(player);
        updateMovementTicks(player);
    }

    private void updateMovementTicks(AbstractClientPlayer player) {
        if (getMovementSpeed() > 0 && !isMovingBackwards() && !player.isCrouching() && !player.isPassenger()) {
            setMovementTicks(getMovementTicks() + 1);
        } else {
            setMovementTicks(0);
        }
    }

    private void updateHandOrientation(HumanoidArm mainHand) {
        if (mainHand == HumanoidArm.RIGHT) {
            setRightHand(MAIN_HAND);
            setLeftHand(OFF_HAND);
        } else {
            setRightHand(OFF_HAND);
            setLeftHand(MAIN_HAND);
        }
    }

    private void updatePlayerPosition(float headYRot, float visualRotationYInDegress, Vec3 playerPosition) {
        setPlayerBodyYaw(visualRotationYInDegress);
        setPlayerHeadYaw(headYRot);
        setPlayerPosition(playerPosition);
        setVectorX((float) (getPlayerPosition().x - getPrevPlayerPosition().x));
        setVectorY((float) (getPlayerPosition().y - getPrevPlayerPosition().y));
        setVectorZ((float) (getPlayerPosition().z - getPrevPlayerPosition().z));
        setMovementSpeed(sqrt(getVectorX() * getVectorX() + getVectorZ() * getVectorZ()));
        setBodyYawDelta(getPlayerBodyYaw() - getPrevPlayerBodyYaw());
    }

    private void updateMovementInfo(float yBodyRot) {
        double bodyYawRadians = toRadians(yBodyRot + 90);
        Vector3f movementVector = new Vector3f(getVectorX(), 0, getVectorZ());
        Vector3f lookVector = new Vector3f((float) cos(bodyYawRadians), 0, (float) sin(bodyYawRadians));
        setMovingBackwards(movementVector.length() > 0 && movementVector.dot(lookVector) < 0);
    }

    private void updateEnvironmentInfo(AbstractClientPlayer player) {
        Block standingBlock = player.level().getBlockState(player.blockPosition().below()).getBlock();
        setOnFence((standingBlock instanceof FenceBlock || standingBlock instanceof WallBlock || standingBlock instanceof IronBarsBlock) && player.onGround());
        setOnEdge(standingBlock instanceof AirBlock && player.onGround());
    }

    private void updateFlyChecker(AbstractClientPlayer player) {
        double flyVectorY = Math.round(getVectorY() * 1000.0) / 1000.0;
        if ((flyVectorY == 0.0 || Math.abs(flyVectorY) == 0.375) && !player.onGround() && !player.isUnderWater()) {
            setFlychecker(getFlychecker() + 1);
        } else if (Math.abs(flyVectorY) > 0.375 || player.onGround()) {
            setFlychecker(0);
        }
    }

    public boolean getPrevMainHandUp() {
        return prevMainHandUp;
    }

    public boolean getPrevOffHandUp() {
        return prevOffHandUp;
    }
}
