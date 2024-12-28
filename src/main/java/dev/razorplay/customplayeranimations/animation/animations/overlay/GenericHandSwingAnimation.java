package dev.razorplay.customplayeranimations.animation.animations.overlay;

import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.*;

import java.io.ObjectInputFilter;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;

public class GenericHandSwingAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        disableArmBasedOnHand(context, context.player().swingingArm);
        context.overlayAnimationContainer().setCurrentAnimationId("hand_swing" + context.overlayAnimationContainer().getCurrentAnimationId());
        context.overlayAnimationContainer().setAnimationFadeTime(0);
        context.overlayAnimationContainer().setAnimationPriority(0);
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().swinging &&
                !(CONFIG.swordAnimations.isEnabled() && context.player().getMainHandItem().getItem() instanceof SwordItem || context.player().getMainHandItem().getItem() instanceof TridentItem) &&
                !(CONFIG.toolsAnimations.axeAnimationsConfig.isEnabled() && context.player().getMainHandItem().getItem() instanceof AxeItem) &&
                !(CONFIG.toolsAnimations.pickaxeAnimationsConfig.isEnabled() && context.player().getMainHandItem().getItem() instanceof PickaxeItem) &&
                !(CONFIG.toolsAnimations.shovelAnimationsConfig.isEnabled() && context.player().getMainHandItem().getItem() instanceof ShovelItem);
    }

    private static void disableArmBasedOnHand(AnimationContext context, InteractionHand hand) {
        context.player().disableBodyPartAnimation(context.mainAnimationContainer(), hand == context.playerData().getRightHand() ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);
        context.player().disableBodyPartAnimation(context.overlayAnimationContainer(), hand == context.playerData().getRightHand() ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);
        context.player().disableBodyPartAnimation(context.specialAnimationContainer(), hand == context.playerData().getRightHand() ? BodyParts.RIGHT_ARM : BodyParts.LEFT_ARM);
    }
}
