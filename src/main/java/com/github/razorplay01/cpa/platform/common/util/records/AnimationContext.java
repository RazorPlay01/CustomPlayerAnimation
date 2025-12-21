package com.github.razorplay01.cpa.platform.common.util.records;

import com.github.razorplay01.cpa.platform.common.animation.AnimationContainer;
import com.github.razorplay01.cpa.platform.common.util.PlayerData;
import com.github.razorplay01.cpa.platform.common.util.interfaces.IAnimationControl;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimatedPlayer;
import net.minecraft.client.player.AbstractClientPlayer;

public record AnimationContext(AnimationContainer mainAnimationContainer, AnimationContainer overlayAnimationContainer,
							   AnimationContainer specialAnimationContainer, /*? if <=1.21.8 {*/AbstractClientPlayer player/*?} else {*/ /*net.minecraft.world.entity.Avatar player*//*?}*/,
							   PlayerData playerData, IAnimationControl iAnimationControl, ICustomAnimatedPlayer iCustomAnimatedPlayer) {
}
