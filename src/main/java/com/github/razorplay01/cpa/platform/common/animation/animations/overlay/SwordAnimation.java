package com.github.razorplay01.cpa.platform.common.animation.animations.overlay;

import com.github.razorplay01.cpa.platform.common.config.ClientConfig;
import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.enums.Modifiers;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.ModTemplate.getAnimation;
import static com.github.razorplay01.cpa.platform.common.util.Util.*;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

//? if >=1.21.1{
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import com.zigythebird.playeranimcore.animation.Animation;
//?}
//? if <1.21.1{
/*import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
*///?}

public class SwordAnimation extends BaseToolSwingAnimation {
	private final Map<UUID, Integer> comboCounts = new HashMap<>();
	private final Map<UUID, Long> lastSwingTicks = new HashMap<>();
	private static final int COMBO_RESET_TICKS = 50;

	@Override
	protected boolean isCorrectItem(ItemStack itemStack) {
		if (isAxe(itemStack) || isPickaxe(itemStack) || isShovel(itemStack)/*? if >=1.21.11 {*/ || isSpear(itemStack)/*?}*/)
			return false;
		return isSword(itemStack) || itemStack.getItem() instanceof TridentItem;
	}

	@Override
	protected ClientConfig.AnimationConfigInterface getConfig() {
		return CONFIG.getOverlayAnimations().swordAnimations.swordAttack1AnimationConfig;
	}

	@Override
	protected AnimationsId getNormalAnimationId() {
		return AnimationsId.SWORD_ATTACK_1_ANIMATION;
	}

	@Override
	protected AnimationsId getSneakAnimationId() {
		return AnimationsId.SWORD_ATTACK_1_SNEAK_ANIMATION;
	}

	@Override
	protected float getAnimationDurationOffset() {
		return -5f;
	}

	@Override
	protected boolean isConfigEnabled() {
		return CONFIG.getOverlayAnimations().swordAnimations.isEnabled();
	}

	@Override
	public void playAnimation(AnimationContext context) {
		UUID uuid = context.player().getUUID();

		if (!isConfigEnabled()) {
			context.overlayAnimationContainer().disableAnimation();
			animationsInProgress.put(uuid, false);
			return;
		}

		// Si el jugador está balanceando la espada
		if (isPlayerSwingingTool(context)) {
			animationStartTimes.put(uuid, context.player().level().getGameTime());
			animationsInProgress.put(uuid, true);
			handleSwordComboAnimation(context);

			// Obtener la duración de la animación actual
			if (context.overlayAnimationContainer().getCurrentAnimation() != null) {
				animationDurations.put(uuid, context.overlayAnimationContainer().getCurrentAnimation()/*? if >=1.21.1 {*/.length()/*?} else {*/ /*.getLength()*//*?}*/ + getAnimationDurationOffset());
			}
		}
		// Si una animación está en progreso
		else if (Boolean.TRUE.equals(animationsInProgress.getOrDefault(uuid, false))) {
			long currentTime = context.player().level().getGameTime();

			if (currentTime - animationStartTimes.getOrDefault(uuid, 0L) < animationDurations.getOrDefault(uuid, 0f)) {
				handleSwordComboAnimation(context);
			} else {
				animationsInProgress.put(uuid, false);
			}
		}
	}

	private void handleSwordComboAnimation(AnimationContext context) {
		UUID uuid = context.player().getUUID();
		long currentTick = context.player().level().getGameTime();

		comboCounts.putIfAbsent(uuid, 0);
		lastSwingTicks.putIfAbsent(uuid, 0L);

		// Resetear combo si ha pasado mucho tiempo
		if (currentTick - lastSwingTicks.get(uuid) > COMBO_RESET_TICKS) {
			comboCounts.put(uuid, 0);
		}

		// IMPORTANTE: Este bloque verifica si hay animación activa
		if (context.overlayAnimationContainer().getAnimationController()/*? if >=1.21.1 {*/.getCurrentAnimation()/*?} else {*//*.getAnimation()*//*?}*/ != null) {
			// SOLO incrementa el combo si está en BLANK
			if (context.overlayAnimationContainer().getAnimationController().isActive() && isBlankAnimation(context)) {
				comboCounts.compute(uuid, (k, currentCount) -> (currentCount % 3) + 1);
			}

			// PERO SIEMPRE actualiza el tick y configura la animación
			lastSwingTicks.put(uuid, currentTick);
			int currentCombo = comboCounts.get(uuid); // Usar GET, no getOrDefault

			// Configurar según el combo actual
			switch (currentCombo) {
				case 2 -> {
					context.overlayAnimationContainer().setAnimationSpeed(CONFIG.getOverlayAnimations().swordAnimations.swordAttack2AnimationConfig.getSpeedMultiplier());
					context.overlayAnimationContainer().setAnimationFadeTime(CONFIG.getOverlayAnimations().swordAnimations.swordAttack2AnimationConfig.getFadeTime());
					context.overlayAnimationContainer().setAnimationPriority(CONFIG.getOverlayAnimations().swordAnimations.swordAttack2AnimationConfig.getPriority());
				}
				case 3 -> {
					context.overlayAnimationContainer().setAnimationSpeed(CONFIG.getOverlayAnimations().swordAnimations.swordAttack3AnimationConfig.getSpeedMultiplier());
					context.overlayAnimationContainer().setAnimationFadeTime(CONFIG.getOverlayAnimations().swordAnimations.swordAttack3AnimationConfig.getFadeTime());
					context.overlayAnimationContainer().setAnimationPriority(CONFIG.getOverlayAnimations().swordAnimations.swordAttack3AnimationConfig.getPriority());
				}
				default -> {
					context.overlayAnimationContainer().setAnimationSpeed(CONFIG.getOverlayAnimations().swordAnimations.swordAttack1AnimationConfig.getSpeedMultiplier());
					context.overlayAnimationContainer().setAnimationFadeTime(CONFIG.getOverlayAnimations().swordAnimations.swordAttack1AnimationConfig.getFadeTime());
					context.overlayAnimationContainer().setAnimationPriority(CONFIG.getOverlayAnimations().swordAnimations.swordAttack1AnimationConfig.getPriority());
				}
			}

			selectComboAnimation(context);
			((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId()))./*? if >=1.21.1 {*/enabled = (context.playerData().getRightHand() != MAIN_HAND)/*?} else {*/ /*setEnabled((context.playerData().getRightHand() != MAIN_HAND))*//*?}*/;
		}
	}

	private void selectComboAnimation(AnimationContext context) {
		boolean isSneaking = context.player().isCrouching();
		UUID uuid = context.player().getUUID();
		int currentCombo = comboCounts.getOrDefault(uuid, 1);

		switch (currentCombo) {
			case 1 -> {
				context.overlayAnimationContainer().setCurrentAnimation(isSneaking ? getAnimation(AnimationsId.SWORD_ATTACK_1_SNEAK_ANIMATION.getAnimationId()) : getAnimation(AnimationsId.SWORD_ATTACK_1_ANIMATION.getAnimationId()));
				context.overlayAnimationContainer().setCurrentAnimationId(isSneaking ? AnimationsId.SWORD_ATTACK_1_SNEAK_ANIMATION.getAnimationId() : AnimationsId.SWORD_ATTACK_1_ANIMATION.getAnimationId());
			}
			case 2 -> {
				context.overlayAnimationContainer().setCurrentAnimation(isSneaking ? getAnimation(AnimationsId.SWORD_ATTACK_2_SNEAK_ANIMATION.getAnimationId()) : getAnimation(AnimationsId.SWORD_ATTACK_2_ANIMATION.getAnimationId()));
				context.overlayAnimationContainer().setCurrentAnimationId(isSneaking ? AnimationsId.SWORD_ATTACK_2_SNEAK_ANIMATION.getAnimationId() : AnimationsId.SWORD_ATTACK_2_ANIMATION.getAnimationId());
			}
			default -> {
				context.overlayAnimationContainer().setCurrentAnimation(isSneaking ? getAnimation(AnimationsId.SWORD_ATTACK_3_SNEAK_ANIMATION.getAnimationId()) : getAnimation(AnimationsId.SWORD_ATTACK_3_ANIMATION.getAnimationId()));
				context.overlayAnimationContainer().setCurrentAnimationId(isSneaking ? AnimationsId.SWORD_ATTACK_3_SNEAK_ANIMATION.getAnimationId() : AnimationsId.SWORD_ATTACK_3_ANIMATION.getAnimationId());
			}
		}
	}

	private boolean isBlankAnimation(AnimationContext context) {
		//? if >=1.21.1{
		return context.overlayAnimationContainer().getAnimationController().getCurrentAnimation().animation() ==
				getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId());
		//?}
		//? if <1.21.1{
		/*return ((KeyframeAnimationPlayer) context.overlayAnimationContainer().getAnimationController().getAnimation())
			.getData().getUuid() == getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId()).getUuid();
		*///?}
	}
}
