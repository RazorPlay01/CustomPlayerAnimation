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
    @ConfigEntry.Gui.Tooltip
    private float animationMoveSpeedMultiplier = 4;

    @ConfigEntry.Category(value = "Animations")
    @ConfigEntry.Gui.CollapsibleObject
    public SwordAnimations swordAnimations = new SwordAnimations();
    @ConfigEntry.Gui.CollapsibleObject
    public HorseAnimations horseAnimationsConfig = new HorseAnimations();
    @ConfigEntry.Gui.CollapsibleObject
    public InWaterAnimations inWaterAnimationsConfig = new InWaterAnimations();
    @ConfigEntry.Gui.CollapsibleObject
    public BoatAnimations boatAnimations = new BoatAnimations();

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
    public AnimationConfig turningSneakAnimationConfig = new AnimationConfig();

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
    public AnimationConfig minecartAnimationsConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig elytraAnimationsConfig = new AnimationConfig();

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
    public AnimationConfig onFenceAnimationConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig onEdgeAnimationConfig = new AnimationConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig upHandAnimationConfig = new AnimationConfig();

    @Getter
    public static class AnimationConfig {
        boolean isEnabled = true;
        float speedMultiplier = 1;
        int fadeTime = 10;
        int priority = 0;
    }

    @Getter
    public static class SwordAnimationConfig {
        float speedMultiplier = 1;
        int fadeTime = 10;
        int priority = 0;
    }

    public static class HorseAnimations {
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig horseIdleAnimationConfig = new AnimationConfig();
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig horseRunningAnimationConfig = new AnimationConfig();
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig horseRunningBackwardsAnimationConfig = new AnimationConfig();
    }

    public static class InWaterAnimations {
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig inWaterUpAnimationConfig = new AnimationConfig();
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig inWaterSwimAnimationConfig = new AnimationConfig();
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig inWaterForwardAnimationConfig = new AnimationConfig();
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig inWaterBackwardsAnimationConfig = new AnimationConfig();
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig inWaterIdleAnimationConfig = new AnimationConfig();
    }

    public static class BoatAnimations {
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig boatTurnAnimationConfig = new AnimationConfig();
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig boatForwardAnimationConfig = new AnimationConfig();
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig boatIdleAnimationConfig = new AnimationConfig();
    }

    public static class SwordAnimations {
        @Getter
        @ConfigEntry.Gui.CollapsibleObject
        boolean isEnabled = true;
        @ConfigEntry.Gui.CollapsibleObject
        public SwordAnimationConfig swordAttack1AnimationConfig = new SwordAnimationConfig();
        @ConfigEntry.Gui.CollapsibleObject
        public SwordAnimationConfig swordAttack2AnimationConfig = new SwordAnimationConfig();
        @ConfigEntry.Gui.CollapsibleObject
        public SwordAnimationConfig swordAttack3AnimationConfig = new SwordAnimationConfig();
    }
}