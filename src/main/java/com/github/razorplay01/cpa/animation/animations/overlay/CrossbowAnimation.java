package com.github.razorplay01.cpa.animation.animations.overlay;

import com.github.razorplay01.cpa.util.enums.BodyParts;
import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.records.AnimationContext;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;

import static com.github.razorplay01.cpa.util.Util.disableBothArms;

public class CrossbowAnimation implements ICustomAnimation {
	private boolean wasMainArmDisabled = false;
	private boolean wasOffArmDisabled = false;
	private ItemStack lastMainHandItem = ItemStack.EMPTY;
	private ItemStack lastOffHandItem = ItemStack.EMPTY;

	private boolean needToForceEnableMainArm = false;
	private boolean needToForceEnableOffArm = false;
	private BodyParts mainArmToEnable = null;
	private BodyParts offArmToEnable = null;

	@Override
	public void playAnimation(AnimationContext context) {
		// Primero, verificar si necesitamos forzar la habilitación de brazos desde el tick anterior
		if (needToForceEnableMainArm && mainArmToEnable != null) {
			context.iAnimationControl().forceEnableBodyPart(mainArmToEnable);
			needToForceEnableMainArm = false;
			mainArmToEnable = null;
		}

		if (needToForceEnableOffArm && offArmToEnable != null) {
			context.iAnimationControl().forceEnableBodyPart(offArmToEnable);
			needToForceEnableOffArm = false;
			offArmToEnable = null;
		}

		// Handle charging (both arms disabled)
		if (context.player().isUsingItem() && context.player().getUseItem().getItem() instanceof CrossbowItem) {
			disableBothArms(context);
			wasMainArmDisabled = true;
			wasOffArmDisabled = true;
			return; // Skip pose checks during charging
		}

		// Track current items
		ItemStack currentMainHandItem = context.player().getMainHandItem();
		ItemStack currentOffHandItem = context.player().getOffhandItem();

		// Determinar qué brazo es cuál
		BodyParts mainArm = context.player().getMainArm() == HumanoidArm.RIGHT ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM;
		BodyParts offArm = context.player().getMainArm() == HumanoidArm.RIGHT ? BodyParts.LEFT_ARM : BodyParts.RIGHT_ARM;

		// Handle holding (disable only the arm holding the crossbow)
		if (context.playerData().getMainArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_HOLD)) {
			context.iAnimationControl().disableBodyPartAnimationInAllContainers(mainArm);
			wasMainArmDisabled = true;
		} else if (wasMainArmDisabled &&
				(!(currentMainHandItem.getItem() instanceof CrossbowItem) ||
						!currentMainHandItem.equals(lastMainHandItem))) {
			// Re-enable main arm if it was disabled and crossbow is no longer held
			wasMainArmDisabled = false;
			needToForceEnableMainArm = true;
			mainArmToEnable = mainArm;
			context.iAnimationControl().forceEnableBodyPart(mainArmToEnable);
		}

		if (context.playerData().getOffArmPose().equals(HumanoidModel.ArmPose.CROSSBOW_HOLD)) {
			context.iAnimationControl().disableBodyPartAnimationInAllContainers(offArm);
			wasOffArmDisabled = true;
		} else if (wasOffArmDisabled &&
				(!(currentOffHandItem.getItem() instanceof CrossbowItem) ||
						!currentOffHandItem.equals(lastOffHandItem))) {
			// Re-enable off arm if it was disabled and crossbow is no longer held
			wasOffArmDisabled = false;
			needToForceEnableOffArm = true;
			offArmToEnable = offArm;
			context.iAnimationControl().forceEnableBodyPart(offArmToEnable);
		}

		// Update last items
		lastMainHandItem = currentMainHandItem.copy();
		lastOffHandItem = currentOffHandItem.copy();
	}

	@Override
	public boolean shouldPlayAnimation(AnimationContext context) {
		// Always check to handle arm re-enabling even when not holding crossbow anymore
		boolean holdingCrossbow = context.player().getMainHandItem().getItem() instanceof CrossbowItem ||
				context.player().getOffhandItem().getItem() instanceof CrossbowItem;

		// Also return true if we need to re-enable arms that were previously disabled
		return holdingCrossbow || wasMainArmDisabled || wasOffArmDisabled;
	}
}
