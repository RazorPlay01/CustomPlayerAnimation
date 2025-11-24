package com.github.razorplay01.cpa.mixin;

import com.github.razorplay01.cpa.platform.common.util.interfaces.ExtendedItemStackRenderState;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemStackRenderState.class)
public class ItemStackRenderStateMixin implements ExtendedItemStackRenderState {
	@Unique
	@Getter
	@Setter
	private ItemStack itemStack = null;
}
