package com.github.razorplay01.cpa.platform.common.animation.animations.overlay;

import com.github.razorplay01.cpa.platform.common.config.ClientConfig;
import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.enums.Modifiers;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.razorplay01.cpa.ModTemplate.getAnimation;
import static com.github.razorplay01.cpa.platform.common.util.Util.configureAnimationContainer;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

//? if >=1.21.1{
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import com.zigythebird.playeranimcore.animation.Animation;
//?}
//? if <1.21.1{
/*import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
*///?}

/**
 * Clase base para animaciones de herramientas que se activan al swing
 * Maneja el tracking de animaciones en progreso y su duración
 */
public abstract class BaseToolSwingAnimation implements ICustomAnimation {
	protected final Map<UUID, Boolean> animationsInProgress = new HashMap<>();
	protected final Map<UUID, Long> animationStartTimes = new HashMap<>();
	protected final Map<UUID, Float> animationDurations = new HashMap<>();

	/**
	 * Verifica si el item en la mano es el tipo correcto para esta animación
	 */
	protected abstract boolean isCorrectItem(ItemStack itemStack);

	/**
	 * Retorna la configuración de animación para esta herramienta
	 */
	protected abstract ClientConfig.AnimationConfigInterface getConfig();

	/**
	 * Retorna el ID de la animación normal (sin agacharse)
	 */
	protected abstract AnimationsId getNormalAnimationId();

	/**
	 * Retorna el ID de la animación agachado
	 */
	protected abstract AnimationsId getSneakAnimationId();

	/**
	 * Retorna el offset de duración de la animación (usualmente negativo)
	 * Este valor se resta de la duración de la animación para determinar cuándo termina
	 */
	protected abstract float getAnimationDurationOffset();

	/**
	 * Verifica si la configuración de esta animación está habilitada
	 */
	protected abstract boolean isConfigEnabled();

	@Override
	public void playAnimation(AnimationContext context) {
		UUID uuid = context.player().getUUID();

		if (!isConfigEnabled()) {
			context.overlayAnimationContainer().disableAnimation();
			animationsInProgress.put(uuid, false);
			return;
		}

		// Si el jugador está balanceando la herramienta
		if (isPlayerSwingingTool(context)) {
			startAnimation(context, uuid);
		}
		// Si una animación está en progreso
		else if (Boolean.TRUE.equals(animationsInProgress.getOrDefault(uuid, false))) {
			continueOrStopAnimation(context, uuid);
		}
	}

	@Override
	public boolean shouldPlayAnimation(AnimationContext context) {
		UUID uuid = context.player().getUUID();
		return (isPlayerSwingingTool(context) ||
				Boolean.TRUE.equals(animationsInProgress.getOrDefault(uuid, false))) &&
				!context.mainAnimationContainer().getCurrentAnimationId()
						.equalsIgnoreCase(AnimationsId.SLEEP_ANIMATION.getAnimationId());
	}

	/**
	 * Verifica si el jugador está balanceando esta herramienta
	 */
	protected boolean isPlayerSwingingTool(AnimationContext context) {
		return context.player().swinging &&
				isCorrectItem(context.player().getMainHandItem()) &&
				context.player().swingingArm.equals(MAIN_HAND);
	}

	/**
	 * Inicia una nueva animación
	 */
	protected void startAnimation(AnimationContext context, UUID uuid) {
		animationStartTimes.put(uuid, context.player().level().getGameTime());
		animationsInProgress.put(uuid, true);

		// Obtener la duración de la animación
		/*? if >=1.21.1 {*/Animation/*?} else {*/ /*KeyframeAnimation*//*?}*/ animation =
				context.player().isCrouching() ?
						getAnimation(getSneakAnimationId().getAnimationId()) :
						getAnimation(getNormalAnimationId().getAnimationId());

		animationDurations.put(uuid, animation/*? if >=1.21.1 {*/.length()/*?} else {*/ /*.getLength()*//*?}*/ + getAnimationDurationOffset());

		applyAnimation(context);
	}

	/**
	 * Continúa la animación si no ha pasado el tiempo mínimo, o la detiene
	 */
	protected void continueOrStopAnimation(AnimationContext context, UUID uuid) {
		long currentTime = context.player().level().getGameTime();
		long elapsedTime = currentTime - animationStartTimes.getOrDefault(uuid, 0L);

		if (elapsedTime < animationDurations.getOrDefault(uuid, 0f)) {
			applyAnimation(context);
		} else {
			animationsInProgress.put(uuid, false);
		}
	}

	/**
	 * Aplica la animación al contenedor
	 */
	protected void applyAnimation(AnimationContext context) {
		boolean isSneaking = context.player().isCrouching();

		configureAnimationContainer(getConfig(), context.overlayAnimationContainer());

		context.overlayAnimationContainer().setCurrentAnimation(
				isSneaking ?
						getAnimation(getSneakAnimationId().getAnimationId()) :
						getAnimation(getNormalAnimationId().getAnimationId())
		);

		context.overlayAnimationContainer().setCurrentAnimationId(
				isSneaking ?
						getSneakAnimationId().getAnimationId() :
						getNormalAnimationId().getAnimationId()
		);

		((MirrorModifier) context.overlayAnimationContainer()
				.getAnimationModifiers()
				.get(Modifiers.MIRROR_MODIFIER.getModifierId()))
				./*? if >=1.21.1 {*/enabled = (context.playerData().getRightHand() != MAIN_HAND)/*?} else {*/ /*setEnabled((context.playerData().getRightHand() != MAIN_HAND))*//*?}*/;
	}
}
