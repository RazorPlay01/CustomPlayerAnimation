//? if >= 1.21.11 {
package com.github.razorplay01.cpa.platform.common.animation.animations.overlay;

import com.github.razorplay01.cpa.platform.common.util.Util;
import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.enums.Modifiers;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.ModTemplate.getAnimation;
import static com.github.razorplay01.cpa.platform.common.util.Util.LEFT_PREFIX;
import static com.github.razorplay01.cpa.platform.common.util.Util.RIGHT_PREFIX;
import static com.github.razorplay01.cpa.platform.common.util.Util.configureAnimationContainer;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class SpearAnimation implements ICustomAnimation {
	private final Map<UUID, Boolean> animationsInProgress = new HashMap<>();
	private final Map<UUID, Long> animationStartTimes = new HashMap<>();
	private final Map<UUID, Float> animationDurations = new HashMap<>();

	@Override
	public void playAnimation(AnimationContext context) {
		UUID uuid = context.player().getUUID();
		if (!CONFIG.getOverlayAnimations().spearAnimations.isEnabled()) {
			context.overlayAnimationContainer().disableAnimation();
			context.iAnimationControl().disableActiveArm(context.mainAnimationContainer());
			animationsInProgress.put(uuid, false);
			return;
		}

		ItemStack mainHand = context.player().getMainHandItem();

		boolean isRightHand;

		if (context.player().isUsingItem()) {
			// Charge: usamos la mano con la que está usando
			isRightHand = context.player().getUsedItemHand().equals(context.playerData().getRightHand());
		} else {
			// Jab: usamos la mano con la que está swinging
			isRightHand = context.player().swingingArm.equals(context.playerData().getRightHand());
		}


		if (Util.isSpear(mainHand)) {
			if (isPlayerAttackingWithSpear(context.player())) {
				// Iniciar animación según tipo de ataque
				animationStartTimes.put(uuid, context.player().level().getGameTime());
				animationsInProgress.put(uuid, true);

				if (context.player().isUsingItem()) {
					// Charge attack (clic derecho sostenido)
					selectChargeAnimation(context, isRightHand);
				} else {
					// Jab attack (clic izquierdo)
					selectJabAnimation(context, isRightHand);
				}

				// Duración, speed, fade, priority desde config
				if (context.overlayAnimationContainer().getCurrentAnimation() != null) {
					animationDurations.put(uuid, context.overlayAnimationContainer().getCurrentAnimation().length() - 5);
				}
			} else if (Boolean.TRUE.equals(animationsInProgress.getOrDefault(uuid, false))) {
				// Continuar hasta que termine
				long currentTime = context.player().level().getGameTime();
				if (currentTime - animationStartTimes.getOrDefault(uuid, 0L) < animationDurations.getOrDefault(uuid, 0f)) {
					// Mantener la animación actual
				} else {
					animationsInProgress.put(uuid, false);
				}
			}


			((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).enabled = !isRightHand;
		}
	}

	@Override
	public boolean shouldPlayAnimation(AnimationContext context) {
		return Util.isSpear(context.player().getMainHandItem()) &&
				(isPlayerAttackingWithSpear(context.player()) || Boolean.TRUE.equals(animationsInProgress.getOrDefault(context.player().getUUID(), false))) &&
				!context.mainAnimationContainer().getCurrentAnimationId().equalsIgnoreCase(AnimationsId.SLEEP_ANIMATION.getAnimationId());
	}

	private boolean isPlayerAttackingWithSpear(Avatar player) {
		ItemStack itemStack = player.getMainHandItem();
		if (!Util.isSpear(itemStack)) return false;

		// Jab (clic izquierdo)
		if (player.swinging && player.swingingArm == MAIN_HAND) {
			return true;
		}

		// Charge (clic derecho sostenido)
		if (player.isUsingItem() && player.getUsedItemHand() == MAIN_HAND) {
			return true;
		}

		return false;
	}

	private void selectJabAnimation(AnimationContext context, boolean isRightHand) {
		if (!CONFIG.getOverlayAnimations().spearAnimations.spearJabAnimationConfig.isEnabled()) {
			context.overlayAnimationContainer().disableAnimation();
			context.iAnimationControl().disableActiveArm(context.mainAnimationContainer());
		} else {
			configureAnimationContainer(CONFIG.getOverlayAnimations().spearAnimations.spearJabAnimationConfig, context.overlayAnimationContainer());
			context.overlayAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.SPEAR_JAB.getAnimationId()));
			context.overlayAnimationContainer().setCurrentAnimationId((isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + AnimationsId.SPEAR_JAB.getAnimationId());
			// Ejemplo: animación normal o sneaking
			//boolean sneaking = context.player().isCrouching();
			//context.overlayAnimationContainer().setCurrentAnimation(
			//		sneaking ? getAnimation(AnimationsId.SPEAR_JAB_SNEAK.getAnimationId()) : getAnimation(AnimationsId.SPEAR_JAB.getAnimationId())
			//);
		}
	}

	private void selectChargeAnimation(AnimationContext context, boolean isRightHand) {
		if (!CONFIG.getOverlayAnimations().spearAnimations.spearChargeAnimationConfig.isEnabled()) {
			context.overlayAnimationContainer().disableAnimation();
			context.iAnimationControl().disableActiveArm(context.mainAnimationContainer());
		} else {
			configureAnimationContainer(CONFIG.getOverlayAnimations().spearAnimations.spearChargeAnimationConfig, context.overlayAnimationContainer());
			context.overlayAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.SPEAR_CHARGE.getAnimationId()));
			context.overlayAnimationContainer().setCurrentAnimationId((isRightHand ? RIGHT_PREFIX : LEFT_PREFIX) + AnimationsId.SPEAR_CHARGE.getAnimationId());
		}
	}
}
//?}
