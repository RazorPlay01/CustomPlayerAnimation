package dev.razorplay.customplayeranimations.util.records;

import dev.kosmx.playerAnim.api.layered.modifier.FirstPersonModifier;
import dev.razorplay.customplayeranimations.animation.AnimationContainer;
import dev.razorplay.customplayeranimations.util.PlayerData;
import net.minecraft.client.player.AbstractClientPlayer;

public record AnimationContext(AnimationContainer mainAnimationContainer, AnimationContainer overlayAnimationContainer,
                               AnimationContainer specialAnimationContainer, AbstractClientPlayer player,
                               PlayerData playerData, FirstPersonModifier firstPersonModifier) {
}
