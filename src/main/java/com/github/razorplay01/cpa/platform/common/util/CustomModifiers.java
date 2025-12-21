package com.github.razorplay01.cpa.platform.common.util;

import com.github.razorplay01.cpa.platform.common.animation.AnimationContainer;
import com.github.razorplay01.cpa.platform.common.config.ClientConfig;
import com.github.razorplay01.cpa.platform.common.util.enums.BodyParts;
import com.github.razorplay01.cpa.platform.common.util.interfaces.IAnimationControl;
import com.zigythebird.playeranimcore.animation.layered.modifier.AdjustmentModifier;
import com.zigythebird.playeranimcore.math.Vec3f;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.platform.common.util.Util.*;

//? if <= 1.21.8 {
import net.minecraft.client.player.AbstractClientPlayer;
//?} else {
//import net.minecraft.world.entity.Avatar;
//?}

public class CustomModifiers {
	private CustomModifiers() {
		// []
	}

	public static AdjustmentModifier createLeanModifier(/*? if <=1.21.8 {*/AbstractClientPlayer player/*?} else {*/ /*Avatar player*//*?}*/) {
		return new AdjustmentModifier(partName -> {
			if (!"body".equals(partName)) {
				return Optional.empty();
			}

			ClientConfig.General config = CONFIG.getGeneral();

			if (!config.isEnableLeanEffect()) {
				return Optional.empty();
			}

			PlayerData data = ((IAnimationControl) player).getAnimationContext().playerData();

			Vec3 velocity = player.getDeltaMovement();

			float bodyYawRad = (float) Math.toRadians(player.yBodyRot);

			float forwardVel = (float) (velocity.z * Math.cos(bodyYawRad) - velocity.x * Math.sin(bodyYawRad));
			float sideVel    = (float) (velocity.z * Math.sin(bodyYawRad) + velocity.x * Math.cos(bodyYawRad));

			// Aplicar inversión si está activada
			if (config.isInvertLeanDirection()) {
				forwardVel = -forwardVel;
				sideVel    = -sideVel;
			}

			// Intensidades independientes
			float leanForward = forwardVel * config.getLeanForwardIntensity();
			float leanSide    = sideVel    * config.getLeanSideIntensity();

			// Multiplicador adicional del PlayerData (opcional, se mantiene)
			leanForward *= data.getLeanMultiplier();
			leanSide    *= data.getLeanMultiplier();

			// Clamps independientes
			leanForward = Mth.clamp(leanForward, -config.getMaxLeanForward(), config.getMaxLeanForward());
			leanSide    = Mth.clamp(leanSide, -config.getMaxLeanSide(), config.getMaxLeanSide());

			// Desactivar en situaciones especiales
			if (player.isFallFlying() || player.getVehicle() != null ||
					player.isVisuallySwimming() || player.isAutoSpinAttack()) {
				return Optional.of(new AdjustmentModifier.PartModifier(
						Vec3f.ZERO,
						Vec3f.ZERO
				));
			}

			// Aplicar rotación: X = lean forward, Z = lean side (negativo para dirección correcta)
			return Optional.of(new AdjustmentModifier.PartModifier(
					new Vec3f(leanForward, 0.0f, -leanSide),
					Vec3f.ZERO
			));
		});
	}

	public static AdjustmentModifier createBowModifier(/*? if <=1.21.8 {*/AbstractClientPlayer player/*?} else {*/ /*Avatar player*//*?}*/) {
		return new AdjustmentModifier(partName -> {
			boolean isUsingBow = player.isUsingItem() && player.getUseItem().getItem() instanceof BowItem;
			if (!isUsingBow) return Optional.empty();
			boolean isRight = (player.getMainArm() != HumanoidArm.RIGHT || !(player.getOffhandItem().getItem() instanceof BowItem)) && (player.getMainArm() != HumanoidArm.LEFT || !(player.getMainHandItem().getItem() instanceof BowItem));

			float pitch = (float) Math.toRadians(player.getXRot());
			if (partName.equals(BodyParts.RIGHT_ARM.getPartId()) || partName.equals(BodyParts.LEFT_ARM.getPartId())) {
				return Optional.of(new AdjustmentModifier.PartModifier(
						new Vec3f(0, 0, isRight ? -pitch : pitch),
						new Vec3f(0, pitch, 0))
				);
			}
			return Optional.empty();
		});
	}

	public static AdjustmentModifier createShieldModifier(/*? if <=1.21.8 {*/AbstractClientPlayer player/*?} else {*/ /*Avatar player*//*?}*/) {
		return new AdjustmentModifier(partName -> {
			boolean isUsingShield = player.isUsingItem() && player.getUseItem().getItem() instanceof ShieldItem;
			if (!isUsingShield) return Optional.empty();

			float limitedPitch = Math.clamp(player.getXRot(), -45, 45);
			float pitch = (float) Math.toRadians(limitedPitch) * 0.5f;

			if (partName.equals(BodyParts.LEFT_ARM.getPartId()) || partName.equals(BodyParts.RIGHT_ARM.getPartId())) {
				return Optional.of(new AdjustmentModifier.PartModifier(
						new Vec3f(pitch, 0, 0),             //rotation
						Vec3f.ZERO                                //position
				));
			}
			return Optional.empty();
		});
	}

	public static AdjustmentModifier createSwingModifier(/*? if <=1.21.8 {*/AbstractClientPlayer player/*?} else {*/ /*Avatar player*//*?}*/, AnimationContainer animationContainer) {
		return new AdjustmentModifier(partName -> {
			if (!isSwingingSwordOrTools(player, animationContainer)) {
				return Optional.empty();
			}

			float pitchRadians = (float) Math.toRadians(player.getXRot());
			//if (FirstPersonMode.isFirstPersonPass()) {
			//	return handleFirstPersonPass(partName, pitchRadians);
			//} else {
			return handleThirdPersonPass(partName, pitchRadians);
			//}
		});
	}
}
