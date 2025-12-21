package com.github.razorplay01.cpa.platform.common.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
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

	// Nuevos campos para lean effect
	private float leanAmount = 0.0f;
	private float realLeanAmount = 0.0f;
	private float leanMultiplier = 1.0f;
	private float realLeanMultiplier = 1.0f;
	private float squash = 0.0f;
	private float realSquash = 0.0f;
	private float prevY = 0.0f;
	private int jumpTicks = 0;

	public void update(/*? if <=1.21.8 {*/AbstractClientPlayer player/*?} else {*/ /*net.minecraft.world.entity.Avatar player*//*?}*/) {
		if (player == null) return;
		updateHandOrientation(player.getMainArm());
		updatePlayerPosition(player.getYHeadRot(), player.getVisualRotationYInDegrees(), player.position());
		updateMovementInfo(player.yBodyRot);
		updateEnvironmentInfo(player);
		updateFlyChecker(player);
		updateMovementTicks(player);
		updateLean(player);
	}

	private void updateMovementTicks(/*? if <=1.21.8 {*/AbstractClientPlayer player/*?} else {*/ /*net.minecraft.world.entity.Avatar player*//*?}*/) {
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

	private void updateEnvironmentInfo(/*? if <=1.21.8 {*/AbstractClientPlayer player/*?} else {*/ /*net.minecraft.world.entity.Avatar player*//*?}*/) {
		Block standingBlock = player.level().getBlockState(player.blockPosition().below()).getBlock();
		setOnFence((standingBlock instanceof FenceBlock || standingBlock instanceof WallBlock || standingBlock instanceof IronBarsBlock) && player.onGround());
		setOnEdge(standingBlock instanceof AirBlock && player.onGround());
	}

	private void updateFlyChecker(/*? if <=1.21.8 {*/AbstractClientPlayer player/*?} else {*/ /*net.minecraft.world.entity.Avatar player*//*?}*/) {
		double flyVectorY = Math.round(getVectorY() * 1000.0) / 1000.0;
		if ((flyVectorY == 0.0 || Math.abs(flyVectorY) == 0.375) && !player.onGround() && !player.isUnderWater()) {
			setFlychecker(getFlychecker() + 1);
		} else if (Math.abs(flyVectorY) > 0.375 || player.onGround()) {
			setFlychecker(0);
		}
	}

	private void updateLean(/*? if <=1.21.8 {*/AbstractClientPlayer player/*?} else {*/ /*net.minecraft.world.entity.Avatar player*//*?}*/) {
		// Multiplicador de intensidad del lean según estado
		double speed = player.getDeltaMovement().horizontalDistance();

		if (player.isCrouching()) {
			realLeanMultiplier = Mth.lerp(0.2f, realLeanMultiplier, 0.6f);  // Menos lean al agacharte
		} else if (speed > 0.2) {
			realLeanMultiplier = Mth.lerp(0.2f, realLeanMultiplier, 1.3f);  // Más lean al correr
		} else {
			realLeanMultiplier = Mth.lerp(0.2f, realLeanMultiplier, 1.0f);  // Normal al caminar
		}

		leanMultiplier = Mth.lerp(0.3f, leanMultiplier, realLeanMultiplier);

		// Reset total si estás quieto o en estados especiales
		if (speed < 0.01 || player.isVisuallySwimming() || player.getVehicle() != null || player.isFallFlying()) {
			realLeanMultiplier = Mth.lerp(0.3f, realLeanMultiplier, 1.0f);
		}

		// === IMPORTANTE: Ya no calculamos leanAmount ni squash ===
		// Dejamos leanAmount en 0 para que no influya el ratón
		realLeanAmount = Mth.lerp(0.4f, realLeanAmount, 0.0f);
		leanAmount = realLeanAmount;

		// Squash siempre en 0
		realSquash = 0.0f;
		squash = 0.0f;
	}

	public boolean getPrevMainHandUp() {
		return prevMainHandUp;
	}

	public boolean getPrevOffHandUp() {
		return prevOffHandUp;
	}
}
