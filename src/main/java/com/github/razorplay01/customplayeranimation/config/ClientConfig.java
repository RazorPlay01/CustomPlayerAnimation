package com.github.razorplay01.customplayeranimation.config;

import com.github.razorplay01.customplayeranimation.CustomPlayerAnimations;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.List;

@Getter
@Config(name = CustomPlayerAnimations.MOD_ID)
public class ClientConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    private float animationSpeedMultiplier = 1;
    @ConfigEntry.Gui.Tooltip
    private List<String> upHandDisableAnimationIds = List.of("bow", "trident", "water", "boat_forward", "boat_turn", "climbing", "sleep", "crawl");

    @ConfigEntry.Category(value = "Animations")
    @ConfigEntry.Gui.CollapsibleObject
    public MoveAnimations moveAnimations = new MoveAnimations();
    @ConfigEntry.Gui.CollapsibleObject
    public IdleAnimations idleAnimations = new IdleAnimations();
    @ConfigEntry.Gui.CollapsibleObject
    public InWaterAnimations inWaterAnimationsConfig = new InWaterAnimations();
    @ConfigEntry.Gui.CollapsibleObject
    public DeathAnimations deathAnimations = new DeathAnimations();

    @ConfigEntry.Gui.CollapsibleObject
    public UseItemAnimation useItemAnimation = new UseItemAnimation();
    @ConfigEntry.Gui.CollapsibleObject
    public SwordAnimations swordAnimations = new SwordAnimations();
    @ConfigEntry.Gui.CollapsibleObject
    public ToolsAnimations toolsAnimations = new ToolsAnimations();

    @ConfigEntry.Gui.CollapsibleObject
    public SpecialAnimations specialAnimations = new SpecialAnimations();
    @ConfigEntry.Gui.CollapsibleObject
    public ExtraAnimations extraAnimations = new ExtraAnimations();
    @ConfigEntry.Gui.CollapsibleObject
    public MountAnimations mountAnimations = new MountAnimations();

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
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

    public static class MountAnimations {
        @ConfigEntry.Gui.CollapsibleObject
        public HorseAnimations horseAnimationsConfig = new HorseAnimations();

        @ConfigEntry.Gui.CollapsibleObject
        public BoatAnimations boatAnimations = new BoatAnimations();

        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig minecartAnimationsConfig = new AnimationConfig();
    }

    public static class ExtraAnimations {
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig fallingAnimationConfig = new AnimationConfig();

        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig climbingAnimationsConfig = new AnimationConfig();

        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig crawlingAnimationsConfig = new AnimationConfig();

        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig elytraAnimationsConfig = new AnimationConfig();

        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig sleepingAnimationsConfig = new AnimationConfig();
    }

    public static class SpecialAnimations {
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig upHandAnimationConfig = new AnimationConfig();
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig itemSwapAnimationConfig = new AnimationConfig(true, 1.5f, 0, 1);
    }

    public static class UseItemAnimation {
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig bowAnimationsConfig = new AnimationConfig();

        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig eatingAnimationsConfig = new AnimationConfig();

        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig shieldAnimationConfig = new AnimationConfig();

        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig tridentAnimationConfig = new AnimationConfig();
    }

    public static class TurnAnimations {
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig turningStandingAnimationConfig = new AnimationConfig();

        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig turningSneakAnimationConfig = new AnimationConfig();
    }

    public static class IdleAnimations {
        @ConfigEntry.Gui.CollapsibleObject
        public TurnAnimations turnAnimations = new TurnAnimations();

        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig idleStandingAnimationConfig = new AnimationConfig();

        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig idleSneakAnimationConfig = new AnimationConfig();

        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig idleCreativeFlyingAnimationConfig = new AnimationConfig();

        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig onFenceAnimationConfig = new AnimationConfig();

        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig onEdgeAnimationConfig = new AnimationConfig();
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

    public static class MoveAnimations {
        @Getter
        @ConfigEntry.Gui.Tooltip
        private float animationMoveSpeedMultiplier = 4;
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig walkingAnimationConfig = new AnimationConfig(true, 1, 3, 0);
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig walkingBackwardsAnimationConfig = new AnimationConfig(true, 1, 3, 0);
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig walkingSneakAnimationConfig = new AnimationConfig(true, 1, 3, 0);
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig walkingSneakBackwardsAnimationConfig = new AnimationConfig(true, 1, 3, 0);
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig runningAnimationConfig = new AnimationConfig();
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig onFenceWalkAnimationConfig = new AnimationConfig(true, 1, 3, 0);
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

    @Getter
    public static class DeathAnimations {
        boolean isEnabled = false;
        float speedMultiplier = 1;
        int fadeTime = 10;
        int priority = 0;

        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig deathBurnAnimationConfig = new AnimationConfig();

        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig deathExplosionAnimationConfig = new AnimationConfig();

        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig deathDrownAnimationConfig = new AnimationConfig();
    }

    public static class ToolsAnimations {
        @Getter
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig pickaxeAnimationsConfig = new AnimationConfig();

        @Getter
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig axeAnimationsConfig = new AnimationConfig();

        @Getter
        @ConfigEntry.Gui.CollapsibleObject
        public AnimationConfig shovelAnimationsConfig = new AnimationConfig();
    }
}