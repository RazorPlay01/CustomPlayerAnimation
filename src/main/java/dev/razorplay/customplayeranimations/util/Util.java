package dev.razorplay.customplayeranimations.util;

import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import dev.kosmx.playerAnim.api.layered.modifier.AdjustmentModifier;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.razorplay.customplayeranimations.animation.AnimationContainer;
import dev.razorplay.customplayeranimations.config.ClientConfig;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomModelData;

import java.util.List;
import java.util.Optional;

import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class Util {
    public static final String RIGHT_PREFIX = "right_";
    public static final String LEFT_PREFIX = "left_";

    private Util() {
        //[]
    }

    public static int getCustomModelDataId(ItemStack itemStack) {
        return Optional.of(itemStack.getComponentsPatch())
                .map(componentsPatch -> (Optional<CustomModelData>) componentsPatch.get(DataComponents.CUSTOM_MODEL_DATA))
                .flatMap(optional -> optional.map(CustomModelData::value))
                .orElse(0);
    }

    public static boolean isBoat(Object vehicle) {
        return vehicle instanceof Boat;
    }

    public static boolean isHorse(Object vehicle) {
        return vehicle instanceof Horse || vehicle instanceof SkeletonHorse || vehicle instanceof ZombieHorse || vehicle instanceof Donkey || vehicle instanceof Mule;
    }

    public static void configureAnimationContainer(ClientConfig.AnimationConfig config, AnimationContainer animationContainer) {
        animationContainer.setAnimationSpeed(config.getSpeedMultiplier());
        animationContainer.setAnimationFadeTime(config.getFadeTime());
        animationContainer.setAnimationPriority(config.getPriority());
    }

    public static boolean containsAnyAnimation(AnimationContainer container, List<String> animations) {
        String currentAnimation = container.getCurrentAnimationId();
        if (animations == null || animations.isEmpty()) {
            return false;
        }
        return animations.stream().anyMatch(currentAnimation::contains);
    }

    public static void disableBothArms(AnimationContext context) {
        context.player().disableBodyPartAnimationInAllContainers(BodyParts.RIGHT_ARM);
        context.player().disableBodyPartAnimationInAllContainers(BodyParts.LEFT_ARM);
    }

    public static void addModifiersToContainer(AnimationContainer container) {
        for (AbstractModifier modifier : container.getAnimationModifiers().values()) {
            container.getAnimationModifierLayer().addModifierLast(modifier);
            if (modifier instanceof MirrorModifier mirrorModifier) {
                mirrorModifier.setEnabled(false);
            }
        }
    }

    public static boolean isSwingingSwordOrTools(AbstractClientPlayer player, AnimationContainer animationContainer) {
        return player.swinging &&
                (player.getMainHandItem().getItem() instanceof ShovelItem ||
                        player.getMainHandItem().getItem() instanceof PickaxeItem ||
                        player.getMainHandItem().getItem() instanceof AxeItem ||
                        player.getMainHandItem().getItem() instanceof SwordItem ||
                        player.getMainHandItem().getItem() instanceof TridentItem) &&
                player.swingingArm.equals(MAIN_HAND) &&
                !animationContainer.getCurrentAnimationId().equalsIgnoreCase(AnimationsId.SLEEP_ANIMATION.getAnimationId());
    }

    public static Optional<AdjustmentModifier.PartModifier> handleFirstPersonPass(String partName, float pitchRadians) {
        float xRot = 0;
        float offsetY = 0;
        float offsetZ = 0;

        if (partName.equals("body")) {
            if (pitchRadians < 0) {
                xRot -= pitchRadians;
                float offset = Math.abs((float) Math.sin(pitchRadians));
                offsetY += offset * 0.5f;
                offsetZ -= offset;
            }
        } else if (partName.equals("rightArm") || partName.equals("leftArm")) {
            xRot = pitchRadians;
        } else {
            return Optional.empty();
        }

        return Optional.of(new AdjustmentModifier.PartModifier(
                new Vec3f(xRot, 0, 0),
                new Vec3f(0, offsetY, offsetZ))
        );
    }

    public static Optional<AdjustmentModifier.PartModifier> handleThirdPersonPass(String partName, float pitchRadians) {
        float xRot = 0;

        switch (partName) {
            case "rightArm", "leftArm" -> xRot += pitchRadians * 0.25F;
            case "body", "rightLeg", "leftLeg" -> xRot -= pitchRadians * 0.50F;
            default -> {
                return Optional.empty();
            }
        }

        return Optional.of(new AdjustmentModifier.PartModifier(
                new Vec3f(xRot, 0, 0),
                new Vec3f(0, 0, 0))
        );
    }
}
