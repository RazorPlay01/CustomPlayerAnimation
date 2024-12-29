package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import dev.razorplay.customplayeranimations.util.enums.Modifiers;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.PotionItem;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.*;

public class EatAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.useItemAnimation.eatingAnimationsConfig.isEnabled()) {
            context.overlayAnimationContainer().disableAnimation();
        } else {
            configureAnimationContainer(CONFIG.useItemAnimation.eatingAnimationsConfig, context.overlayAnimationContainer());

            if (context.player().getUsedItemHand().equals(context.playerData().getRightHand())) {
                setEatingAnimation(context, BodyParts.RIGHT_ARM, false);
            } else if (context.player().getUsedItemHand().equals(context.playerData().getLeftHand())) {
                setEatingAnimation(context, BodyParts.LEFT_ARM, true);
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().isUsingItem() && context.player().getUseItem().getItem().components().get(DataComponents.FOOD) != null || context.player().getUseItem().getItem() instanceof PotionItem;
    }

    private static void setEatingAnimation(AnimationContext context, BodyParts arm, boolean mirror) {
        context.overlayAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.EAT_ANIMATION.getAnimationId()));
        context.overlayAnimationContainer().setCurrentAnimationId((mirror ? LEFT_PREFIX : RIGHT_PREFIX) + AnimationsId.EAT_ANIMATION.getAnimationId());
        ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).setEnabled(mirror);
    }
}
