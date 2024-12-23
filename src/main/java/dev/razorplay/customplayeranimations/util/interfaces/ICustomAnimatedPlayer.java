package dev.razorplay.customplayeranimations.util.interfaces;

import dev.razorplay.customplayeranimations.animation.AnimationContainer;

public interface ICustomAnimatedPlayer {
    AnimationContainer getMainAnimationCPA();

    AnimationContainer getOverlayAnimationCPA();

    AnimationContainer getSpecialAnimationCPA();
}
