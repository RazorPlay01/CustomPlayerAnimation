package dev.razorplay.customplayeranimations.util.interfaces;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;

public interface ICustomAnimatedPlayer {
    ModifierLayer<IAnimation> getMainAnimationCPA();

    ModifierLayer<IAnimation> getOverlayAnimationCPA();
}
