package com.github.razorplay01.cpa.platform.common.util.interfaces;

import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;

public interface ICustomAnimation {
	void playAnimation(AnimationContext context);

	boolean shouldPlayAnimation(AnimationContext context);
}
