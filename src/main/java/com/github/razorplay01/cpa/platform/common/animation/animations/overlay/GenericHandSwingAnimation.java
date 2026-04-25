package com.github.razorplay01.cpa.platform.common.animation.animations.overlay;

import com.github.razorplay01.cpa.platform.common.util.enums.BodyParts;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.*;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;

public class GenericHandSwingAnimation implements ICustomAnimation {
	// Rastrear si el brazo estaba deshabilitado en el tick anterior
	private boolean wasArmDisabled = false;
	private InteractionHand lastSwingingArm = null;

	// Rastrear si necesitamos forzar la habilitación del brazo en el próximo tick
	private boolean needToForceEnableArm = false;
	private BodyParts armToEnable = null;

	@Override
	public void playAnimation(AnimationContext context) {
		// Primero, verificar si necesitamos forzar la habilitación del brazo desde el tick anterior
		if (needToForceEnableArm && armToEnable != null) {
			context.iAnimationControl().forceEnableBodyPart(armToEnable);
			needToForceEnableArm = false;
			armToEnable = null;
		}

		// Si el jugador está golpeando, deshabilitar el brazo correspondiente
		if (context.player().swinging) {
			BodyParts swingingArm = context.player().swingingArm == context.playerData().getRightHand()
					? BodyParts.RIGHT_ARM
					: BodyParts.LEFT_ARM;
			context.iAnimationControl().disableBodyPartAnimationInAllContainers(swingingArm);
			wasArmDisabled = true;
			lastSwingingArm = context.player().swingingArm;
		} else if (wasArmDisabled && !context.player().swinging) {
			// Si el jugador ya no está golpeando pero el brazo estaba deshabilitado
			wasArmDisabled = false;
			needToForceEnableArm = true;
			armToEnable = lastSwingingArm == context.playerData().getRightHand()
					? BodyParts.RIGHT_ARM
					: BodyParts.LEFT_ARM;
			context.iAnimationControl().forceEnableBodyPart(armToEnable);
		}

		context.overlayAnimationContainer().setCurrentAnimationId("hand_swing" + context.overlayAnimationContainer().getCurrentAnimationId());
		context.overlayAnimationContainer().setAnimationFadeTime(0);
		context.overlayAnimationContainer().setAnimationPriority(0);
	}

	@Override
	public boolean shouldPlayAnimation(AnimationContext context) {
		// Siempre verificar para manejar la reactivación del brazo incluso cuando ya no está golpeando
		boolean isSwinging = context.player().swinging &&
				!(CONFIG.getOverlayAnimations().swordAnimations.isEnabled() &&
						//? if < 1.21.2 {
						/*context.player().getMainHandItem().getItem() instanceof net.minecraft.world.item.SwordItem ||
						*///?}
						//? if >= 1.21.2 {
						context.player().getMainHandItem().getItem().getDefaultInstance().getComponents().has(net.minecraft.core.component.DataComponents.WEAPON) ||
						//?}
						context.player().getMainHandItem().getItem() instanceof TridentItem) &&
				!(CONFIG.getOverlayAnimations().toolsAnimations.axeAnimationsConfig.isEnabled() &&
						context.player().getMainHandItem().getItem() instanceof AxeItem) &&
				//? if >=1.21.1{
				!(CONFIG.getOverlayAnimations().toolsAnimations.pickaxeAnimationsConfig.isEnabled() &&
						context.player().getMainHandItem().getItem().getDefaultInstance().getComponents().has(net.minecraft.core.component.DataComponents.TOOL)) &&
				//?}
				//? if <1.21.1{
				/*!(CONFIG.getOverlayAnimations().toolsAnimations.pickaxeAnimationsConfig.isEnabled() && context.player().getMainHandItem().getItem() instanceof PickaxeItem) &&
				 *///?}
				!(CONFIG.getOverlayAnimations().toolsAnimations.shovelAnimationsConfig.isEnabled() &&
						context.player().getMainHandItem().getItem() instanceof ShovelItem);

		// También devolver true si necesitamos reactivar un brazo que estaba previamente deshabilitado
		return isSwinging || wasArmDisabled || needToForceEnableArm;
	}
}
