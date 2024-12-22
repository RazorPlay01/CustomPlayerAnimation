package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.disableBodyPart;

public class ItemSwapAnimation {
    private ItemSwapAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if ((!context.player().getMainHandItem().isEmpty() || !context.player().getOffhandItem().isEmpty())
                && context.playerData().getMainHandItem().getItem() != context.playerData().getOffHandItem().getItem()
                && context.playerData().getMainHandItem().getItem() == context.player().getOffhandItem().getItem()
                && context.playerData().getOffHandItem().getItem() == context.player().getMainHandItem().getItem()) {
            System.out.println("AAAAAAAAA");
            context.upHandAnimationContainer().setAnimationFadeTime(0);
            context.upHandAnimationContainer().setAnimationSpeed(0.5f);
            context.upHandAnimationContainer().setAnimationPriority(1);
            context.upHandAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.ITEM_SWAP_ANIMATION.getAnimationId()));
            context.upHandAnimationContainer().setCurrentAnimationId(AnimationsId.ITEM_SWAP_ANIMATION.getAnimationId());
            disableBodyPart(context.mainAnimationContainer(), BodyParts.RIGHT_ARM);
            disableBodyPart(context.mainAnimationContainer(), BodyParts.LEFT_ARM);
        }
        context.playerData().setMainHandItem(context.player().getMainHandItem());
        context.playerData().setOffHandItem(context.player().getOffhandItem());
    }

}
