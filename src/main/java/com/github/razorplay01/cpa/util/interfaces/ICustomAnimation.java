package com.github.razorplay01.cpa.util.interfaces;

import com.github.razorplay01.cpa.util.records.AnimationContext;

public interface ICustomAnimation {
    void playAnimation(AnimationContext context);
    boolean shouldPlayAnimation(AnimationContext context);
}
