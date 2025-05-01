package dev.razorplay.customplayeranimations.animation.animations.extras;

import dev.razorplay.customplayeranimations.util.Util;
import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.configureAnimationContainer;

public class BoxIdleStandingAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.idleAnimations.idleStandingAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
        } else {
            configureAnimationContainer(CONFIG.idleAnimations.idleStandingAnimationConfig, context.mainAnimationContainer());

            context.mainAnimationContainer().setCurrentAnimation(getAnimation("podadora_idle_animation"));
            context.mainAnimationContainer().setCurrentAnimationId("podadora_idle_animation");
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        ItemStack mainHandItem = context.player().getMainHandItem();
        ItemStack offHandItem = context.player().getOffhandItem();
        return context.playerData().getMovementSpeed() == 0 &&
                context.playerData().getBodyYawDelta() == 0 &&
                !context.player().isCrouching() &&
                !context.player().isPassenger() &&
                ((mainHandItem.is(Items.PAPER) && Util.getCustomModelDataId(mainHandItem) == 1194 ||
                        mainHandItem.is(Items.PAPER) && Util.getCustomModelDataId(mainHandItem) == 1199 ||
                        mainHandItem.is(Items.PAPER) && Util.getCustomModelDataId(mainHandItem) == 1200 ||
                        mainHandItem.is(Items.PAPER) && Util.getCustomModelDataId(mainHandItem) == 1201) ||
                        (offHandItem.is(Items.PAPER) && Util.getCustomModelDataId(offHandItem) == 1194 ||
                                offHandItem.is(Items.PAPER) && Util.getCustomModelDataId(offHandItem) == 1199 ||
                                offHandItem.is(Items.PAPER) && Util.getCustomModelDataId(offHandItem) == 1200 ||
                                offHandItem.is(Items.PAPER) && Util.getCustomModelDataId(offHandItem) == 1201));
    }
}
