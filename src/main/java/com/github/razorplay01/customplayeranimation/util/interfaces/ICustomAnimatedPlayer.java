package com.github.razorplay01.customplayeranimation.util.interfaces;

import com.github.razorplay01.customplayeranimation.animation.AnimationContainer;

public interface ICustomAnimatedPlayer {
    AnimationContainer getMainAnimationCPA();

    AnimationContainer getOverlayAnimationCPA();

    AnimationContainer getSpecialAnimationCPA();
}
