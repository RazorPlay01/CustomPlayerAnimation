package com.github.razorplay01.cpa.util.records;

import com.github.razorplay01.cpa.animation.AnimationContainer;
import com.github.razorplay01.cpa.util.PlayerData;
import com.github.razorplay01.cpa.util.interfaces.IAnimationControl;
import com.github.razorplay01.cpa.util.interfaces.ICustomAnimatedPlayer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Avatar;

public record AnimationContext(AnimationContainer mainAnimationContainer, AnimationContainer overlayAnimationContainer,
                               AnimationContainer specialAnimationContainer, Avatar avatar,
                               PlayerData playerData, IAnimationControl iAnimationControl, ICustomAnimatedPlayer iCustomAnimatedPlayer) {
}
