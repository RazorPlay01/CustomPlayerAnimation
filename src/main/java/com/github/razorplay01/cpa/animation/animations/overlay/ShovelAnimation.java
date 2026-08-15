package com.github.razorplay01.cpa.animation.animations.overlay;

import com.github.razorplay01.cpa.config.ClientConfig;
import com.github.razorplay01.cpa.util.enums.AnimationsId;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.util.Util.isShovel;

public class ShovelAnimation extends BaseToolSwingAnimation {
	@Override
	protected boolean isCorrectItem(ItemStack itemStack) {
		return isShovel(itemStack) && itemStack.getItem() instanceof ShovelItem;
	}

	@Override
	protected ClientConfig.AnimationConfigInterface getConfig() {
		return CONFIG.getOverlayAnimations().toolsAnimations.shovelAnimationsConfig;
	}

	@Override
	protected AnimationsId getNormalAnimationId() {
		return AnimationsId.SHOVEL_ANIMATION;
	}

	@Override
	protected AnimationsId getSneakAnimationId() {
		return AnimationsId.SHOVEL_SNEAK_ANIMATION;
	}

	@Override
	protected float getAnimationDurationOffset() {
		return -4f;
	}

	@Override
	protected boolean isConfigEnabled() {
		return CONFIG.getOverlayAnimations().toolsAnimations.shovelAnimationsConfig.isEnabled();
	}
}
