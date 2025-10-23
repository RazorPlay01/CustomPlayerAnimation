package com.github.razorplay01.cpa.util;

import com.github.razorplay01.cpa.animation.AnimationContainer;
import com.github.razorplay01.cpa.util.enums.BodyParts;
import com.zigythebird.playeranimcore.animation.layered.modifier.AdjustmentModifier;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonMode;
import com.zigythebird.playeranimcore.math.Vec3f;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ShieldItem;

import java.util.Optional;

import static com.github.razorplay01.cpa.util.Util.*;

public class CustomModifiers {
    private CustomModifiers() {
        // []
    }

    public static AdjustmentModifier createBowModifier(Avatar avatar) {
        return new AdjustmentModifier(partName -> {
            boolean isUsingBow = avatar.isUsingItem() && avatar.getUseItem().getItem() instanceof BowItem;
            if (!isUsingBow) return Optional.empty();
            boolean isRight = (avatar.getMainArm() != HumanoidArm.RIGHT || !(avatar.getOffhandItem().getItem() instanceof BowItem)) && (avatar.getMainArm() != HumanoidArm.LEFT || !(avatar.getMainHandItem().getItem() instanceof BowItem));

            float pitch = (float) Math.toRadians(avatar.getXRot());
            if (partName.equals(BodyParts.RIGHT_ARM.getPartId()) || partName.equals(BodyParts.LEFT_ARM.getPartId())) {
                return Optional.of(new AdjustmentModifier.PartModifier(
                        new Vec3f(0, 0, isRight ? -pitch : pitch),
                        new Vec3f(0, pitch, 0))
                );
            }
            return Optional.empty();
        });
    }

    public static AdjustmentModifier createShieldModifier(Avatar avatar) {
        return new AdjustmentModifier(partName -> {
            boolean isUsingShield = avatar.isUsingItem() && avatar.getUseItem().getItem() instanceof ShieldItem;
            if (!isUsingShield) return Optional.empty();

            float limitedPitch = Math.clamp(avatar.getXRot(), -45, 45);
            float pitch = (float) Math.toRadians(limitedPitch) * 0.5f;

            if (partName.equals(BodyParts.LEFT_ARM.getPartId()) || partName.equals(BodyParts.RIGHT_ARM.getPartId())) {
                return Optional.of(new AdjustmentModifier.PartModifier(
                        new Vec3f(pitch, 0, 0),             //rotation
                        Vec3f.ZERO                                //position
                ));
            }
            return Optional.empty();
        });
    }

    public static AdjustmentModifier createSwingModifier(Avatar avatar, AnimationContainer animationContainer) {
        return new AdjustmentModifier(partName -> {
            if (!isSwingingSwordOrTools(avatar, animationContainer)) {
                return Optional.empty();
            }

            float pitchRadians = (float) Math.toRadians(avatar.getXRot());
            if (FirstPersonMode.isFirstPersonPass()) {
                return handleFirstPersonPass(partName, pitchRadians);
            } else {
                return handleThirdPersonPass(partName, pitchRadians);
            }
        });
    }
}
