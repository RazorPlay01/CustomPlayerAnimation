package dev.razorplay.customplayeranimations.util;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;

public interface ICustomAnimatedPlayer {
    ModifierLayer<IAnimation> customPlayerAnimations_getModAnimation();

}
