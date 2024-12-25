package dev.razorplay.customplayeranimations.animation.animations.special;

import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;

public class ItemSwapAnimation implements ICustomAnimation {
    public void playAnimation(AnimationContext context) {
        context.specialAnimationContainer().setAnimationFadeTime(0);
        context.specialAnimationContainer().setAnimationSpeed(0.5f);
        context.specialAnimationContainer().setAnimationPriority(1);
        context.specialAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.ITEM_SWAP_ANIMATION.getAnimationId()));
        context.specialAnimationContainer().setCurrentAnimationId(AnimationsId.ITEM_SWAP_ANIMATION.getAnimationId());
        context.player().disableBodyPartAnimation(context.mainAnimationContainer(), BodyParts.RIGHT_ARM);
        context.player().disableBodyPartAnimation(context.mainAnimationContainer(), BodyParts.LEFT_ARM);

        context.playerData().setMainHandItem(context.player().getMainHandItem());
        context.playerData().setOffHandItem(context.player().getOffhandItem());
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return (!context.player().getMainHandItem().isEmpty() || !context.player().getOffhandItem().isEmpty())
                && context.playerData().getMainHandItem().getItem() != context.playerData().getOffHandItem().getItem()
                && context.playerData().getMainHandItem().getItem() == context.player().getOffhandItem().getItem()
                && context.playerData().getOffHandItem().getItem() == context.player().getMainHandItem().getItem();
    }
}
