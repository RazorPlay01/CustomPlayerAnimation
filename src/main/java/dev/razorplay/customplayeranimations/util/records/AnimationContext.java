package dev.razorplay.customplayeranimations.util.records;

import dev.razorplay.customplayeranimations.animation.AnimationContainer;
import dev.razorplay.customplayeranimations.util.PlayerData;
import net.minecraft.client.player.AbstractClientPlayer;

public record AnimationContext(AnimationContainer mainAnimationContainer, AnimationContainer overlayAnimationContainer,
                               AnimationContainer specialAnimationContainer, AbstractClientPlayer player, PlayerData playerData) {
}
