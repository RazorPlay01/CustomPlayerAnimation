package dev.razorplay.customplayeranimations.animation;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

import static dev.razorplay.customplayeranimations.animation.AnimationProvider.BLANK_LOOP_ANIMATION;


@Getter
@Setter
@AllArgsConstructor
public class AnimationContainer {
    private final ModifierLayer<IAnimation> animationModifierLayer;
    private final HashMap<String, AbstractModifier> animationModifiers;
    private KeyframeAnimation currentAnimation;
    private String currentAnimationId;
    private String prevAnimationId;
    private int animationFadeTime;
    private int animationPriority;
    private int prevAnimationPriority;
    private float animationSpeed;

    public void disableAnimation() {
        this.setCurrentAnimation(BLANK_LOOP_ANIMATION.animation());
        this.setCurrentAnimationId(BLANK_LOOP_ANIMATION.animationId());
    }
}