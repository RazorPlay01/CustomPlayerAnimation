package com.github.razorplay01.cpa.platform.common.util.interfaces;

import net.minecraft.world.entity.LivingEntity;

public interface HumanoidRenderStateAccessor {
	void setLivingEntity(LivingEntity entity);

	LivingEntity getLivingEntity();
}
