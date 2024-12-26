package dev.razorplay.customplayeranimations.animation.animations.special;

import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.razorplay.customplayeranimations.animation.AnimationContainer;
import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import dev.razorplay.customplayeranimations.util.enums.Modifiers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Set;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.*;

public class UpHandAnimation implements ICustomAnimation {
    private static final Set<Item> UP_HAND_ITEMS = Set.of(
            Items.TORCH, Items.SOUL_TORCH, Items.REDSTONE_TORCH,
            Items.FILLED_MAP, Items.RECOVERY_COMPASS, Items.COMPASS
    );
    private static boolean lastMainHandState = false;
    private static boolean lastOffHandState = false;


    public void playAnimation(AnimationContext context) {
        if (!CONFIG.specialAnimations.upHandAnimationConfig.isEnabled()) {
            context.specialAnimationContainer().disableAnimation();
        } else {
            HandStates handStates = determineHandStates(context.player());
            handleHandStateChange(handStates, context.mainAnimationContainer());
            if (shouldPlayHandAnimation(handStates, context)) {
                playHandAnimations(handStates, context);
            } else {
                context.specialAnimationContainer().disableAnimation();
            }
            updateLastHandStates(handStates);
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return true;
    }

    private static HandStates determineHandStates(AbstractClientPlayer player) {
        return new HandStates(
                isHandUp(player.getMainHandItem()),
                isHandUp(player.getOffhandItem())
        );
    }

    private static void handleHandStateChange(HandStates currentStates, AnimationContainer mainContainer) {
        if (currentStates.hasChanged(lastMainHandState, lastOffHandState)) {
            mainContainer.disableAnimation();
        }
    }

    private static boolean shouldPlayHandAnimation(HandStates states, AnimationContext context) {
        return (states.isMainHandUp || states.isOffHandUp) &&
                !isBlockingAnimation(context.overlayAnimationContainer(), context.mainAnimationContainer());
    }

    private static boolean isBlockingAnimation(AnimationContainer overlay, AnimationContainer main) {
        return containsAnyAnimation(overlay, CONFIG.getUpHandDisableAnimationIds()) ||
                containsAnyAnimation(main, CONFIG.getUpHandDisableAnimationIds());
    }

    private static void playHandAnimations(HandStates states, AnimationContext context) {
        if (states.isMainHandUp) {
            setUpHandAnimation(context,
                    context.player().getMainArm());
        }
        if (states.isOffHandUp) {
            setUpHandAnimation(context,
                    getOppositeArm(context.player().getMainArm()));
        }
    }

    private static HumanoidArm getOppositeArm(HumanoidArm arm) {
        return arm == HumanoidArm.RIGHT ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
    }

    private static void updateLastHandStates(HandStates states) {
        lastMainHandState = states.isMainHandUp;
        lastOffHandState = states.isOffHandUp;
    }

    private static void setUpHandAnimation(AnimationContext context, HumanoidArm arm) {
        context.specialAnimationContainer().setAnimationFadeTime(10);
        context.specialAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.UP_HAND_ANIMATION.getAnimationId()));
        String animationId = (arm == HumanoidArm.RIGHT ? RIGHT_PREFIX : LEFT_PREFIX) + AnimationsId.UP_HAND_ANIMATION.getAnimationId();
        context.specialAnimationContainer().setCurrentAnimationId(animationId);
        context.player().disableBodyPartAnimation(context.mainAnimationContainer(), arm == HumanoidArm.RIGHT ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);
        ((MirrorModifier) context.specialAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).setEnabled(arm == HumanoidArm.RIGHT);
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
