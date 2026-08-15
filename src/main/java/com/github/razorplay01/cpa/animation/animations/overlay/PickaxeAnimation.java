package com.github.razorplay01.cpa.animation.animations.overlay;

import com.github.razorplay01.cpa.config.ClientConfig;
import com.github.razorplay01.cpa.util.enums.AnimationsId;
import net.minecraft.world.item.ItemStack;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.util.Util.isPickaxe;

public class PickaxeAnimation extends BaseToolSwingAnimation {
	@Override
	protected boolean isCorrectItem(ItemStack itemStack) {
		return isPickaxe(itemStack) &&
				//? if >=1.21.1{
				itemStack.getItem().getDefaultInstance().getComponents().has(net.minecraft.core.component.DataComponents.TOOL)
				//?}
				//? if <1.21.1{
				/*itemStack.getItem() instanceof net.minecraft.world.item.PickaxeItem
				 *///?}
				;
	}

	@Override
	protected ClientConfig.AnimationConfigInterface getConfig() {
		return CONFIG.getOverlayAnimations().toolsAnimations.pickaxeAnimationsConfig;
	}

	@Override
	protected AnimationsId getNormalAnimationId() {
		return AnimationsId.PICKAXE_ANIMATION;
	}

	@Override
	protected AnimationsId getSneakAnimationId() {
		return AnimationsId.PICKAXE_SNEAK_ANIMATION;
	}

	@Override
	protected float getAnimationDurationOffset() {
		return -3f;
	}

	@Override
	protected boolean isConfigEnabled() {
		return CONFIG.getOverlayAnimations().toolsAnimations.pickaxeAnimationsConfig.isEnabled();
	}
}
