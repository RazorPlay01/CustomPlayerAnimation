package com.github.razorplay01.cpa.platform.common.util;

import com.github.razorplay01.cpa.platform.common.animation.AnimationContainer;
import com.github.razorplay01.cpa.platform.common.util.enums.BodyParts;
import com.zigythebird.playeranimcore.animation.layered.modifier.AdjustmentModifier;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonMode;
import com.zigythebird.playeranimcore.math.Vec3f;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ShieldItem;

import java.util.Optional;

import static com.github.razorplay01.cpa.platform.common.util.Util.*;

//? if <= 1.21.8 {
//import net.minecraft.client.player.AbstractClientPlayer;
//?} else {
import net.minecraft.world.entity.Avatar;
//?}

public class CustomModifiers {
	private CustomModifiers() {
		// []
	}

	public static AdjustmentModifier createBowModifier(/*? if <=1.21.8 {*//*AbstractClientPlayer player*//*?} else {*/ Avatar player/*?}*/) {
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

	public static AdjustmentModifier createShieldModifier(/*? if <=1.21.8 {*//*AbstractClientPlayer player*//*?} else {*/ Avatar player/*?}*/) {
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

	public static AdjustmentModifier createSwingModifier(/*? if <=1.21.8 {*//*AbstractClientPlayer player*//*?} else {*/ Avatar player/*?}*/, AnimationContainer animationContainer) {
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
