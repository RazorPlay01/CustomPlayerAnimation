package com.github.razorplay01.customplayeranimation.animation.animations.special;

import com.github.razorplay01.customplayeranimation.animation.AnimationContainer;
import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.enums.Modifiers;
import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Set;

import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.customplayeranimation.util.Util.*;

public class UpHandAnimation implements ICustomAnimation {
    private static final Set<Item> UP_HAND_ITEMS = Set.of(
            Items.TORCH, Items.SOUL_TORCH, Items.REDSTONE_TORCH,
            Items.FILLED_MAP, Items.RECOVERY_COMPASS, Items.COMPASS
    );

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

    private static HandStates determineHandStates(AbstractClientPlayer player) {
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
        return containsAnyAnimation(overlay, CONFIG.getGeneral().getUpHandDisableAnimationIds()) ||
                containsAnyAnimation(main, CONFIG.getGeneral().getUpHandDisableAnimationIds());
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
        ((MirrorModifier) context.specialAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).enabled = isMainRightArm;
    }

    private static boolean isHandUp(ItemStack itemStack) {
        return UP_HAND_ITEMS.contains(itemStack.getItem());
    }

    record HandStates(boolean isMainHandUp, boolean isOffHandUp) {
        boolean hasChanged(boolean lastMainState, boolean lastOffState) {
            return (lastMainState != isMainHandUp) || (lastOffState != isOffHandUp);
        }
    }
}