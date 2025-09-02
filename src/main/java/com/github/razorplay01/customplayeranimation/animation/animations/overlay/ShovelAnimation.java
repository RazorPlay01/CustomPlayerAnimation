package com.github.razorplay01.customplayeranimation.animation.animations.overlay;

import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.enums.Modifiers;
import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

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
            ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).enabled = (context.playerData().getRightHand() != MAIN_HAND);
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().swinging &&
                isShovel(context.player().getMainHandItem()) &&
                context.player().getMainHandItem().getItem() instanceof ShovelItem &&
                context.player().swingingArm.equals(MAIN_HAND) &&
                !context.mainAnimationContainer().getCurrentAnimationId().equalsIgnoreCase(AnimationsId.SLEEP_ANIMATION.getAnimationId());
    }

    public boolean isShovel(ItemStack itemStack) {
        Tool tool = itemStack.get(DataComponents.TOOL);
        if (tool != null) {
            // Usa un BlockState representativo que pertenezca a BlockTags.MINEABLE_WITH_SHOVEL
            BlockState dirtState = Blocks.DIRT.defaultBlockState();
            return tool.isCorrectForDrops(dirtState);
        }
        return false;
    }
}
