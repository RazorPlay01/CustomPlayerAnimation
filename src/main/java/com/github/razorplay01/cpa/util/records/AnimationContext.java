package com.github.razorplay01.cpa.util.records;

import com.github.razorplay01.cpa.animation.AnimationContainer;
import com.github.razorplay01.cpa.util.PlayerData;
import net.minecraft.client.player.AbstractClientPlayer;

public record AnimationContext(AnimationContainer mainAnimationContainer, AnimationContainer overlayAnimationContainer,
                               AnimationContainer specialAnimationContainer, AbstractClientPlayer player,
                               PlayerData playerData) {
}
