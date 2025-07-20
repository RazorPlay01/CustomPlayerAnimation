package com.github.razorplay01.customplayeranimation.util.records;

import com.github.razorplay01.customplayeranimation.animation.AnimationContainer;
import com.github.razorplay01.customplayeranimation.util.PlayerData;
import net.minecraft.client.player.AbstractClientPlayer;

public record AnimationContext(AnimationContainer mainAnimationContainer, AnimationContainer overlayAnimationContainer,
                               AnimationContainer specialAnimationContainer, AbstractClientPlayer player,
                               PlayerData playerData) {
}
