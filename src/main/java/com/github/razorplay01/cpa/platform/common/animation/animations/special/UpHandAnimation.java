package com.github.razorplay01.cpa.platform.common.animation.animations.special;

import com.github.razorplay01.cpa.platform.common.animation.AnimationContainer;
import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.enums.Modifiers;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.resources.Identifier;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.ModTemplate.getAnimation;
import static com.github.razorplay01.cpa.platform.common.util.Util.*;


//? if >=1.21.1{
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
//?}
//? if <1.21.1{
/*import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
*///?}

public class UpHandAnimation implements ICustomAnimation {
	public void playAnimation(AnimationContext context) {
		HandStates handStates = determineHandStates(context.player());
		handleHandStateChange(handStates, context.mainAnimationContainer(), context);
		boolean configEnabled = CONFIG.getSpecialAnimations().upHandAnimationConfig.isEnabled();
		boolean shouldPlay = configEnabled && shouldPlayHandAnimation(handStates, context);
		if (shouldPlay) {
			playHandAnimations(handStates, context);
		} else {
			String currId = context.specialAnimationContainer().getCurrentAnimationId();
			if (currId.contains(AnimationsId.UP_HAND_ANIMATION.getAnimationId())) {
				context.specialAnimationContainer().disableAnimation();
			}
		}
		updateLastHandStates(handStates, context);
	}

	@Override
	public boolean shouldPlayAnimation(AnimationContext context) {
		return true;  // Siempre considera, la lógica de config está en playAnimation para poder deshabilitar si es necesario.
	}

	private static HandStates determineHandStates(
			//? if <= 1.21.8 {
			/*net.minecraft.client.player.AbstractClientPlayer player
			*///?} else {
			net.minecraft.world.entity.Avatar player
			//?}
			) {
		return new HandStates(
				isHandUp(player.getMainHandItem()),
				isHandUp(player.getOffhandItem())
		);
	}

	private static void handleHandStateChange(HandStates currentStates, AnimationContainer mainContainer, AnimationContext context) {
		boolean prevMain = context.playerData().getPrevMainHandUp();
		boolean prevOff = context.playerData().getPrevOffHandUp();
		if (currentStates.hasChanged(prevMain, prevOff)) {
			mainContainer.disableAnimation();
		}
	}

	private static boolean shouldPlayHandAnimation(HandStates states, AnimationContext context) {
		return (states.isMainHandUp || states.isOffHandUp) &&
				!isBlockingAnimation(context.overlayAnimationContainer(), context.mainAnimationContainer());
	}

	private static boolean isBlockingAnimation(AnimationContainer overlay, AnimationContainer main) {
		return containsAnyAnimation(overlay, CONFIG.getSpecialAnimations().getUpHandDisableAnimationIds()) ||
				containsAnyAnimation(main, CONFIG.getSpecialAnimations().getUpHandDisableAnimationIds());
	}

	private static void playHandAnimations(HandStates states, AnimationContext context) {
		if (states.isMainHandUp) {
			setUpHandAnimation(context, context.player().getMainArm());
		}
		if (states.isOffHandUp) {
			setUpHandAnimation(context, getOppositeArm(context.player().getMainArm()));
		}
	}

	private static HumanoidArm getOppositeArm(HumanoidArm arm) {
		return arm == HumanoidArm.RIGHT ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
	}

	private static void updateLastHandStates(HandStates states, AnimationContext context) {
		context.playerData().setPrevMainHandUp(states.isMainHandUp);
		context.playerData().setPrevOffHandUp(states.isOffHandUp);
	}

	private static void setUpHandAnimation(AnimationContext context, HumanoidArm arm) {
		context.specialAnimationContainer().setAnimationSpeed(CONFIG.getSpecialAnimations().upHandAnimationConfig.getSpeedMultiplier());
		context.specialAnimationContainer().setAnimationFadeTime(CONFIG.getSpecialAnimations().upHandAnimationConfig.getFadeTime());
		context.specialAnimationContainer().setAnimationPriority(CONFIG.getSpecialAnimations().upHandAnimationConfig.getPriority());
		context.specialAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.UP_HAND_ANIMATION.getAnimationId()));
		boolean isMainRightArm = arm == HumanoidArm.RIGHT;
		String animationId = (isMainRightArm ? RIGHT_PREFIX : LEFT_PREFIX) + AnimationsId.UP_HAND_ANIMATION.getAnimationId();
		context.specialAnimationContainer().setCurrentAnimationId(animationId);
		((MirrorModifier) context.specialAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId()))./*? if >=1.21.1 {*/enabled = isMainRightArm/*?} else {*/ /*setEnabled(isMainRightArm)*//*?}*/;
	}

	private static boolean isHandUp(ItemStack itemStack) {
		return getUpHandItems().contains(itemStack.getItem());
	}

	record HandStates(boolean isMainHandUp, boolean isOffHandUp) {
		boolean hasChanged(boolean lastMainState, boolean lastOffState) {
			return (lastMainState != isMainHandUp) || (lastOffState != isOffHandUp);
		}
	}

	public static Set<Item> getUpHandItems() {
		return CONFIG.getSpecialAnimations().upHandItemIds.stream()
				.map(idStr -> {
					Identifier id;
					if (idStr.contains(":")) {
						id = Identifier.tryParse(idStr);
					} else {
						//? if >=1.21.1{
						id = Identifier.fromNamespaceAndPath("minecraft", idStr.toLowerCase());
						//?}
						//? if <1.21.1{
						/*id = new Identifier("minecraft", idStr.toLowerCase());
						*///?}
					}


					//? if <= 1.21.1 {
					/*if (id != null) {
						Item item = BuiltInRegistries.ITEM.get(id);
						if (item != Items.AIR) {
							return item;
						}
					}
					*///?}
					//? if >= 1.21.2 {
					if (id != null && BuiltInRegistries.ITEM.get(id).isPresent()) {
						Item item = BuiltInRegistries.ITEM.get(id).get().value();
						if (item != Items.AIR) {
							return item;
						}
					}
					//?}

					return null;
				})
				.filter(Objects::nonNull)
				.collect(Collectors.toUnmodifiableSet());
	}
}
