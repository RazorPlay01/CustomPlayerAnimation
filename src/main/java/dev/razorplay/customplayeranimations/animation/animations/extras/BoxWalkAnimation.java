package dev.razorplay.customplayeranimations.animation.animations.extras;

import dev.razorplay.customplayeranimations.util.Util;
import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;

public class BoxWalkAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.moveAnimations.walkingAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
        } else {
            context.mainAnimationContainer().setAnimationSpeed((float) (context.playerData().getMovementSpeed() * CONFIG.moveAnimations.getAnimationMoveSpeedMultiplier() * CONFIG.moveAnimations.walkingAnimationConfig.getSpeedMultiplier()));
            context.mainAnimationContainer().setAnimationFadeTime(CONFIG.moveAnimations.walkingAnimationConfig.getFadeTime());
            context.mainAnimationContainer().setAnimationPriority(CONFIG.moveAnimations.walkingAnimationConfig.getPriority());

            context.mainAnimationContainer().setCurrentAnimation(getAnimation("podadora_walking_animation"));
            context.mainAnimationContainer().setCurrentAnimationId("podadora_walking_animation");
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        ItemStack mainHandItem = context.player().getMainHandItem();
        ItemStack offHandItem = context.player().getOffhandItem();
        return context.playerData().getMovementSpeed() > 0 &&
                !context.playerData().isMovingBackwards() &&
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
