package com.github.razorplay01.cpa.mixin;
//? if >= 1.21.2 {

import com.github.razorplay01.cpa.platform.common.util.interfaces.HumanoidRenderStateAccessor;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(HumanoidRenderState.class)
public class HumanoidRenderStateMixin implements HumanoidRenderStateAccessor {
	@Unique
	private LivingEntity entity;

	@Override
	public void setLivingEntity(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public LivingEntity getLivingEntity() {
		return entity;
	}
}
//?}
