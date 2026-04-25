package com.github.razorplay01.cpa.platform.common.animation.animations.overlay;

import com.github.razorplay01.cpa.platform.common.config.ClientConfig;
import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.platform.common.util.Util.isAxe;

public class AxeAnimation extends BaseToolSwingAnimation {
	@Override
	protected boolean isCorrectItem(ItemStack itemStack) {
		return isAxe(itemStack) && itemStack.getItem() instanceof AxeItem;
	}

	@Override
	protected ClientConfig.AnimationConfigInterface getConfig() {
		return CONFIG.getOverlayAnimations().toolsAnimations.axeAnimationsConfig;
	}

	@Override
	protected AnimationsId getNormalAnimationId() {
		return AnimationsId.AXE_ANIMATION;
	}

	@Override
	protected AnimationsId getSneakAnimationId() {
		return AnimationsId.AXE_SNEAK_ANIMATION;
	}

	@Override
	protected float getAnimationDurationOffset() {
		return -3f;
	}

	@Override
	protected boolean isConfigEnabled() {
		return CONFIG.getOverlayAnimations().toolsAnimations.axeAnimationsConfig.isEnabled();
	}
}
