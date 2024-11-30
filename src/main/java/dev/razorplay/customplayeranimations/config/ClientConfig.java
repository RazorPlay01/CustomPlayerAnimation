package dev.razorplay.customplayeranimations.config;

import dev.razorplay.customplayeranimations.CustomPlayerAnimations;
import lombok.Getter;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Getter
@Config(name = CustomPlayerAnimations.MOD_ID)
public class ClientConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    private float animationSpeedMultiplier = 1;

    @ConfigEntry.Category(value = "Animations")

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig walkingAnimationConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig walkingBackwardsAnimationConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig walkingSneakAnimationConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig walkingSneakBackwardsAnimationConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig runningAnimationConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig turningStandingAnimationConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig idleStandingAnimationConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig idleSneakAnimationConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig idleCreativeFlyingAnimationConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig fallingAnimationConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig climbingAnimationsConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig crawlingAnimationsConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig inWaterAnimationsConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig minecartAnimationsConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig horseIdleAnimationConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig horseRunningAnimationConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig boatAnimationsConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig elytraAnimationsConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig swordAttackAnimationsConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig bowAnimationsConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig pickaxeAnimationsConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig axeAnimationsConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig shovelAnimationsConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig sleepingAnimationsConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig eatingAnimationsConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig shieldAnimationConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig tridentAnimationConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig onFenceWalkAnimationConfig = new AnimationConfig();

    @Getter
    public static class AnimationConfig {
        boolean isEnabled = true;
        float speedMultiplier = 1;
    }
}