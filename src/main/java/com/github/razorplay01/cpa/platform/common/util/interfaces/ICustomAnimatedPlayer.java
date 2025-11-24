package com.github.razorplay01.cpa.platform.common.util.interfaces;

import com.github.razorplay01.cpa.platform.common.animation.AnimationContainer;

public interface ICustomAnimatedPlayer {
	AnimationContainer getMainAnimationCPA();

	AnimationContainer getOverlayAnimationCPA();

	AnimationContainer getSpecialAnimationCPA();
}
