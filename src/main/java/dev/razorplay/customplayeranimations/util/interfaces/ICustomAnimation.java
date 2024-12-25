package dev.razorplay.customplayeranimations.util.interfaces;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;

public interface ICustomAnimation {
    void playAnimation(AnimationContext context);
    boolean shouldPlayAnimation(AnimationContext context);
}
