package com.github.razorplay01.customplayeranimation.animation.animations.overlay;

import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;
import net.minecraft.core.component.DataComponents;

import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.customplayeranimation.util.Util.configureAnimationContainer;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class PickaxeAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.toolsAnimations.pickaxeAnimationsConfig.isEnabled()) {
            context.overlayAnimationContainer().disableAnimation();
        } else {
            configureAnimationContainer(CONFIG.toolsAnimations.pickaxeAnimationsConfig, context.overlayAnimationContainer());

            context.overlayAnimationContainer().setCurrentAnimation(context.player().isCrouching() ? getAnimation(AnimationsId.PICKAXE_SNEAK_ANIMATION.getAnimationId()) : getAnimation(AnimationsId.PICKAXE_ANIMATION.getAnimationId()));
            context.overlayAnimationContainer().setCurrentAnimationId(context.player().isCrouching() ? AnimationsId.PICKAXE_SNEAK_ANIMATION.getAnimationId() : AnimationsId.PICKAXE_ANIMATION.getAnimationId());
            //todo: ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).setEnabled(context.playerData().getRightHand() != MAIN_HAND);
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().swinging &&
                context.player().getMainHandItem().getItem().getDefaultInstance().getComponents().has(DataComponents.TOOL) &&
                context.player().swingingArm.equals(MAIN_HAND) &&
                !context.mainAnimationContainer().getCurrentAnimationId().equalsIgnoreCase(AnimationsId.SLEEP_ANIMATION.getAnimationId());
    }
}
