package dev.razorplay.customplayeranimations.util;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.modifier.AdjustmentModifier;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.razorplay.customplayeranimations.animation.AnimationContainer;
import dev.razorplay.customplayeranimations.util.enums.BodyParts;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ShieldItem;

import java.util.Optional;

import static dev.razorplay.customplayeranimations.util.Util.*;

public class CustomModifiers {
    private CustomModifiers() {
        // []
    }

    public static AdjustmentModifier createBowModifier(AbstractClientPlayer player) {
        return new AdjustmentModifier(partName -> {
            boolean isUsingBow = player.isUsingItem() && player.getUseItem().getItem() instanceof BowItem;
            if (!isUsingBow) return Optional.empty();
            boolean isRight = (player.getMainArm() != HumanoidArm.RIGHT || !(player.getOffhandItem().getItem() instanceof BowItem)) && (player.getMainArm() != HumanoidArm.LEFT || !(player.getMainHandItem().getItem() instanceof BowItem));

            float pitch = (float) Math.toRadians(player.getXRot());
            if (partName.equals(BodyParts.RIGHT_ARM.getPartId()) || partName.equals(BodyParts.LEFT_ARM.getPartId())) {
                return Optional.of(new AdjustmentModifier.PartModifier(
                        new Vec3f(0, 0, isRight ? -pitch : pitch),
                        new Vec3f(0, pitch, 0))
                );
            }
            return Optional.empty();
        });
    }

    public static AdjustmentModifier createShieldModifier(AbstractClientPlayer player) {
        return new AdjustmentModifier(partName -> {
            boolean isUsingShield = player.isUsingItem() && player.getUseItem().getItem() instanceof ShieldItem;
            if (!isUsingShield) return Optional.empty();

            float limitedPitch = Math.clamp(player.getXRot(), -45, 45);
            float pitch = (float) Math.toRadians(limitedPitch) * 0.5f;

            if (partName.equals(BodyParts.LEFT_ARM.getPartId()) || partName.equals(BodyParts.RIGHT_ARM.getPartId())) {
                return Optional.of(new AdjustmentModifier.PartModifier(
                        new Vec3f(pitch, 0, 0),                   //rotation
                        Vec3f.ZERO                                //position
                ));
            }
            return Optional.empty();
        });
    }

    public static AdjustmentModifier createSwingModifier(AbstractClientPlayer player, AnimationContainer animationContainer) {
        return new AdjustmentModifier(partName -> {
            if (!isSwingingSwordOrTools(player, animationContainer)) {
                return Optional.empty();
            }

            float pitchRadians = (float) Math.toRadians(player.getXRot());
            if (FirstPersonMode.isFirstPersonPass()) {
                return handleFirstPersonPass(partName, pitchRadians);
            } else {
                return handleThirdPersonPass(partName, pitchRadians);
            }
        });
    }
}
