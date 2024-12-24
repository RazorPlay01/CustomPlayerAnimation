package dev.razorplay.customplayeranimations.animation;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;

public interface ICustomAnimation {
    void playAnimation(AnimationContext context);
    boolean shouldPlayAnimation(AnimationContext context);
}
