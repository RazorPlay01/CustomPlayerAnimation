package com.github.razorplay01.customplayeranimation.animation.animations.overlay;

import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.enums.Modifiers;
import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

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
            ((MirrorModifier) context.overlayAnimationContainer().getAnimationModifiers().get(Modifiers.MIRROR_MODIFIER.getModifierId())).enabled = (context.playerData().getRightHand() != MAIN_HAND);
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().swinging &&
                isPickaxe(context.player().getMainHandItem()) &&
                context.player().getMainHandItem().getItem().getDefaultInstance().getComponents().has(DataComponents.TOOL) &&
                context.player().swingingArm.equals(MAIN_HAND) &&
                !context.mainAnimationContainer().getCurrentAnimationId().equalsIgnoreCase(AnimationsId.SLEEP_ANIMATION.getAnimationId());
    }

    public boolean isPickaxe(ItemStack itemStack) {
        Tool tool = itemStack.get(DataComponents.TOOL);
        if (tool != null) {
            // Usa un BlockState representativo que pertenezca a BlockTags.MINEABLE_WITH_PICKAXE
            BlockState stoneState = Blocks.STONE.defaultBlockState();
            return tool.isCorrectForDrops(stoneState);
        }
        return false;
    }
}
