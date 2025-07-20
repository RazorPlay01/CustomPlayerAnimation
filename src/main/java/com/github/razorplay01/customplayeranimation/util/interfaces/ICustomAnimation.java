package com.github.razorplay01.customplayeranimation.util.interfaces;

import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;

public interface ICustomAnimation {
    void playAnimation(AnimationContext context);
    boolean shouldPlayAnimation(AnimationContext context);
}
