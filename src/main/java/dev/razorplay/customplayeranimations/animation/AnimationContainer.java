package dev.razorplay.customplayeranimations.animation;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import dev.kosmx.playerAnim.api.layered.modifier.AdjustmentModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Vec3f;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public class AnimationContainer {
    private final ModifierLayer<IAnimation> animationModifierLayer;
    private final List<AbstractModifier> animationModifiers;
    private KeyframeAnimation currentAnimation;
    private String currentAnimationId;
    private String prevAnimationId;
    private int animationFadeTime;
    private int animationPriority;
    private int prevAnimationPriority;
}
