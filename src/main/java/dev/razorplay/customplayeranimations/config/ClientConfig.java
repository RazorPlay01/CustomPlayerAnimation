package dev.razorplay.customplayeranimations.config;

import dev.razorplay.customplayeranimations.CustomPlayerAnimations;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = CustomPlayerAnimations.MOD_ID)
public class ClientConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public float animationSpeedMultiplier = 1;

    @ConfigEntry.Category(value = "Animations")

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig walkingAnimationConfig = new AnimationConfig();

    public AnimationConfig getWalkingAnimationConfig() {
        return walkingAnimationConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig walkingBackwardsAnimationConfig = new AnimationConfig();

    public AnimationConfig getWalkingBackwardsAnimationConfig() {
        return walkingBackwardsAnimationConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig walkingSneakAnimationConfig = new AnimationConfig();

    public AnimationConfig getWalkingSneakAnimationConfig() {
        return walkingSneakAnimationConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig walkingSneakBackwardsAnimationConfig = new AnimationConfig();

    public AnimationConfig getWalkingSneakBackwardsAnimationConfig() {
        return walkingSneakBackwardsAnimationConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig runningAnimationConfig = new AnimationConfig();

    public AnimationConfig getRunningAnimationConfig() {
        return runningAnimationConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig turningStandingAnimationConfig = new AnimationConfig();

    public AnimationConfig getTurningStandingAnimationConfig() {
        return turningStandingAnimationConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig idleStandingAnimationConfig = new AnimationConfig();

    public AnimationConfig getIdleStandingAnimationConfig() {
        return idleStandingAnimationConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig idleSneakAnimationConfig = new AnimationConfig();

    public AnimationConfig getIdleSneakAnimationConfig() {
        return idleSneakAnimationConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig idleCreativeFlyingAnimationConfig = new AnimationConfig();

    public AnimationConfig getIdleCreativeFlyingAnimationConfig() {
        return idleCreativeFlyingAnimationConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig fallingAnimationConfig = new AnimationConfig();

    public AnimationConfig getFallingAnimationConfig() {
        return fallingAnimationConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig climbingAnimationsConfig = new AnimationConfig();

    public AnimationConfig getClimbingAnimationsConfig() {
        return climbingAnimationsConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig crawlingAnimationsConfig = new AnimationConfig();

    public AnimationConfig getCrawlingAnimationsConfig() {
        return crawlingAnimationsConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig inWaterAnimationsConfig = new AnimationConfig();

    public AnimationConfig getInWaterAnimationsConfig() {
        return inWaterAnimationsConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig minecartAnimationsConfig = new AnimationConfig();

    public AnimationConfig getMinecartAnimationsConfig() {
        return minecartAnimationsConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig horseIdleAnimationConfig = new AnimationConfig();

    public AnimationConfig getHorseIdleAnimationConfig() {
        return horseIdleAnimationConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig horseRunningAnimationConfig = new AnimationConfig();

    public AnimationConfig getHorseRunningAnimationConfig() {
        return horseRunningAnimationConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig boatAnimationsConfig = new AnimationConfig();

    public AnimationConfig getBoatAnimationsConfig() {
        return boatAnimationsConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig elytraAnimationsConfig = new AnimationConfig();

    public AnimationConfig getElytraAnimationsConfig() {
        return elytraAnimationsConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig swordAttackAnimationsConfig = new AnimationConfig();

    public AnimationConfig getSwordAttackAnimationsConfig() {
        return swordAttackAnimationsConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig bowAnimationsConfig = new AnimationConfig();

    public AnimationConfig getBowAnimationsConfig() {
        return bowAnimationsConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig pickaxeAnimationsConfig = new AnimationConfig();

    public AnimationConfig getPickaxeAnimationsConfig() {
        return pickaxeAnimationsConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig axeAnimationsConfig = new AnimationConfig();

    public AnimationConfig getAxeAnimationsConfig() {
        return axeAnimationsConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig shovelAnimationsConfig = new AnimationConfig();

    public AnimationConfig getShovelAnimationsConfig() {
        return shovelAnimationsConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig sleepingAnimationsConfig = new AnimationConfig();

    public AnimationConfig getSleepingAnimationsConfig() {
        return sleepingAnimationsConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig eatingAnimationsConfig = new AnimationConfig();

    public AnimationConfig getEatingAnimationsConfig() {
        return eatingAnimationsConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig shieldAnimationConfig = new AnimationConfig();

    public AnimationConfig getShieldAnimationConfig() {
        return shieldAnimationConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig tridentAnimationConfig = new AnimationConfig();

    public AnimationConfig getTridentAnimationConfig() {
        return tridentAnimationConfig;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AnimationConfig onFenceWalkAnimationConfig = new AnimationConfig();

    public AnimationConfig getOnFenceWalkAnimationConfig() {
        return onFenceWalkAnimationConfig;
    }

    public static class AnimationConfig {
        boolean isEnabled = true;
        float speedMultiplier = 1;

        public boolean isEnabled() {
            return isEnabled;
        }

        public float getSpeedMultiplier() {
            return speedMultiplier;
        }
    }
}