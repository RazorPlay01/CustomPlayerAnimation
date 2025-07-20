package com.github.razorplay01.customplayeranimation.animation.animations.overlay;

import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;
import net.minecraft.world.item.ShovelItem;

import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.customplayeranimation.util.Util.configureAnimationContainer;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class ShovelAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.toolsAnimations.shovelAnimationsConfig.isEnabled()) {
            context.overlayAnimationContainer().disableAnimation();
        } else {
            configureAnimationContainer(CONFIG.toolsAnimations.shovelAnimationsConfig, context.overlayAnimationContainer());

            context.overlayAnimationContainer().setCurrentAnimation(context.player().isCrouching() ? getAnimation(AnimationsId.SHOVEL_SNEAK_ANIMATION.getAnimationId()) : getAnimation(AnimationsId.SHOVEL_ANIMATION.getAnimationId()));
            context.overlayAnimationContainer().setCurrentAnimationId(context.player().isCrouching() ? AnimationsId.SHOVEL_SNEAK_ANIMATION.getAnimationId() : AnimationsId.SHOVEL_ANIMATION.getAnimationId());
            //todo: ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).setEnabled(context.playerData().getRightHand() != MAIN_HAND);
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().swinging &&
                context.player().getMainHandItem().getItem() instanceof ShovelItem &&
                context.player().swingingArm.equals(MAIN_HAND) &&
                !context.mainAnimationContainer().getCurrentAnimationId().equalsIgnoreCase(AnimationsId.SLEEP_ANIMATION.getAnimationId());
    }
}
