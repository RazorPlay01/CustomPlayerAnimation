package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import dev.razorplay.customplayeranimations.util.enums.Modifiers;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.PotionItem;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.*;

public class EatAnimation {
    private EatAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isUsingItem() && context.player().getUseItem().getItem().components().get(DataComponents.FOOD) != null || context.player().getUseItem().getItem() instanceof PotionItem) {
            if (!CONFIG.eatingAnimationsConfig.isEnabled()) {
                context.overlayAnimationContainer().disableAnimation();
            } else {
                configureAnimationContainer(CONFIG.eatingAnimationsConfig, context.overlayAnimationContainer());

                if (context.player().getUsedItemHand().equals(context.playerData().getRightHand())) {
                    setEatingAnimation(context, BodyParts.RIGHT_ARM, false);
                } else if (context.player().getUsedItemHand().equals(context.playerData().getLeftHand())) {
                    setEatingAnimation(context, BodyParts.LEFT_ARM, true);
                }
            }
        }
    }

    private static void setEatingAnimation(AnimationContext context, BodyParts arm, boolean mirror) {
        context.overlayAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.EAT_ANIMATION.getAnimationId()));
        context.overlayAnimationContainer().setCurrentAnimationId((mirror ? LEFT_PREFIX : RIGHT_PREFIX) + AnimationsId.EAT_ANIMATION.getAnimationId());
        disableBodyPart(context.mainAnimationContainer(), arm);
        ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).setEnabled(mirror);
    }
}
