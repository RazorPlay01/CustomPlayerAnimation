package com.github.razorplay01.cpa.platform.common.animation.animations.overlay;

import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.enums.Modifiers;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;
import net.minecraft.client.player.AbstractClientPlayer;
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
public class SwordAnimation implements ICustomAnimation {
	private final Map<UUID, Integer> comboCounts = new HashMap<>();
	private final Map<UUID, Long> lastSwingTicks = new HashMap<>();
	private final int COMBO_RESET_TICKS = 50;

	private final Map<UUID, Boolean> animationsInProgress = new HashMap<>();
	private final Map<UUID, Long> animationStartTimes = new HashMap<>();
	private final Map<UUID, Float> animationDurations = new HashMap<>();

	@Override
	public void playAnimation(AnimationContext context) {
		UUID uuid = context.player().getUUID();
		if (!CONFIG.getOverlayAnimations().swordAnimations.isEnabled()) {
			context.overlayAnimationContainer().disableAnimation();
			animationsInProgress.put(uuid, false);
		} else {
			// Si el jugador está balanceando el arma o la animación está en progreso
			if (isPlayerSwingingWeapon(context.player())) {
				// Iniciar una nueva animación
				animationStartTimes.put(uuid, context.player().level().getGameTime());
				animationsInProgress.put(uuid, true);
				handleSwordComboAnimation(context);

				// Obtener la duración de la animación actual después de seleccionarla
				if (context.overlayAnimationContainer().getCurrentAnimation() != null) {
					animationDurations.put(uuid, (float) (context.overlayAnimationContainer().getCurrentAnimation()/*? if >=1.21.1 {*/.length()/*?} else {*//*.getLength()*//*?}*/ - 5));
				}
			} else if (Boolean.TRUE.equals(animationsInProgress.getOrDefault(uuid, false))) {
				// Continuar la animación si no ha pasado el tiempo mínimo
				long currentTime = context.player().level().getGameTime();
				if (currentTime - animationStartTimes.getOrDefault(uuid, 0L) < animationDurations.getOrDefault(uuid, 0f)) {
					handleSwordComboAnimation(context);
				} else {
					// La animación ha terminado
					animationsInProgress.put(uuid, false);
				}
			}
		}
	}

	@Override
	public boolean shouldPlayAnimation(AnimationContext context) {
		UUID uuid = context.player().getUUID();
		// Reproducir la animación si el jugador está balanceando el arma o si una animación está en progreso
		return (isPlayerSwingingWeapon(context.player()) || Boolean.TRUE.equals(animationsInProgress.getOrDefault(uuid, false))) &&
				!context.mainAnimationContainer().getCurrentAnimationId().equalsIgnoreCase(AnimationsId.SLEEP_ANIMATION.getAnimationId());
	}

	private void handleSwordComboAnimation(AnimationContext context) {
		UUID uuid = context.player().getUUID();
		long currentTick = context.player().level().getGameTime();

		comboCounts.putIfAbsent(uuid, 0);
		lastSwingTicks.putIfAbsent(uuid, 0L);

		if (currentTick - lastSwingTicks.get(uuid) > COMBO_RESET_TICKS) {
			comboCounts.put(uuid, 0);
		}

		if (context.overlayAnimationContainer().getAnimationController()/*? if >=1.21.1 {*/.getCurrentAnimation()/*?} else {*//*.getAnimation()*//*?}*/ != null) {
			if (context.overlayAnimationContainer().getAnimationController().isActive() &&
					//? if >=1.21.1{
					context.overlayAnimationContainer().getAnimationController().getCurrentAnimation().animation() == getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId())
					//?}
					//? if <1.21.1{
					/*((KeyframeAnimationPlayer) context.overlayAnimationContainer().getAnimationController().getAnimation()).getData().getUuid() == getAnimation(AnimationsId.BLANK_LOOP_ANIMATION.getAnimationId()).getUuid()
				*///?}

			) {
				comboCounts.compute(uuid, (k, currentCount) -> (currentCount % 3) + 1);
			}

			lastSwingTicks.put(uuid, currentTick);
			int currentCombo = comboCounts.get(uuid);
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

	public static boolean isPlayerSwingingWeapon(/*? if <=1.21.8 {*//*AbstractClientPlayer player*//*?} else {*/ net.minecraft.world.entity.Avatar player/*?}*/) {
		ItemStack itemStack = player.getMainHandItem();
		if (isAxe(itemStack) || isPickaxe(itemStack) || isShovel(itemStack)/*? if >=1.21.11 {*/ || isSpear(itemStack)/*?}*/)
			return false;
		return player.swinging &&
				(isSword(itemStack) || itemStack.getItem() instanceof TridentItem) &&
				player.swingingArm.equals(MAIN_HAND);
	}
}
